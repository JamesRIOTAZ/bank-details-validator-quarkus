package bankdetailsvalidator.quarkus.validationexception;

import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RuleFive implements Rule {

    /**
     * String for how the MOD11 check is specified in the modulus weight table.
     */
    private static final String MOD11 = "MOD11";

    /**
     * String for how the DBLAL check is specified in the modulus weight table.
     */
    private static final String DBLAL = "DBLAL";

    /*
     * If the sorting code appears in this table in the "Original s/c" column, substitute it for the "substitute with" column (for check
     * purposes only). If the sorting code is not found, use the original sorting code.
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {

        String substitutionSortCode = bankDetails.getSortCode();

        if (sortCodeSubstitutionTable.containsKey(bankDetails.getSortCode())) {
            final String replaceWith = sortCodeSubstitutionTable.get(bankDetails.getSortCode());
            substitutionSortCode = replaceWith;

        }

        /*
         * For the standard check with exception 5 the checkdigit is g from the original account number: - if the remainder=0 and g=0 the
         * account number is valid - if the remainder=1 the account number is invalid - for all other remainders, take the remainder away
         * from 11. If the number you get is the same as g then the account number is valid. For the double alternate check with exception 5
         * the checkdigit is h from the original account number: - if the remainder=0 and h=0 the account number is valid - for all other
         * remainders, take the remainder away from 10. If the number you get is the same as h then the account number is valid.
         */
        final int remainder = modulusChecker.getRemainder(substitutionSortCode+bankDetails.getAccountNumber(), entry.getWeights());
        if (entry.getAlgorithm().equals(MOD11)) {
            processMOD11(validationResult, bankDetails, remainder);
        } else if (entry.getAlgorithm().equals(DBLAL)) {
            processDBLAL(validationResult, bankDetails, remainder);
        }
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
            validationResult.setComplete(true);
        }
        return validationResult;
    }

    private void processDBLAL(ValidationResult validationResult, SimpleBankDetailsModel bankDetails, int remainder) {
        if (bankDetails.charAt('h') == '0' && remainder == 0) {
            validationResult.setCheckPassed(true);
        } else {
            validationResult.setCheckPassed((10 - remainder) == bankDetails.intAt('h'));
        }
    }

    private void processMOD11(ValidationResult validationResult, SimpleBankDetailsModel bankDetails, int remainder) {
        if (bankDetails.charAt('g') == '0' && remainder == 0) {
            validationResult.setCheckPassed(true);
        } else if (remainder == 1) {
            validationResult.setCheckPassed(false);
        } else {
            validationResult.setCheckPassed((11 - remainder) == bankDetails.intAt('g'));
        }
    }
}
