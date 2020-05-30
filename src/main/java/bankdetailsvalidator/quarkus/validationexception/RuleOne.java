package bankdetailsvalidator.quarkus.validationexception;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleOne implements Rule {

    /*
     * Place a financial institution number (580149) before the sorting code and account number string which is subject to the alternate
     * doubling as well
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {

        final String SubstitutionBankDetails = "580149"+bankDetails.getSortCode()+bankDetails.getAccountNumber();
        final int[] alternateDoublingForPrefix = new int[]{1, 2, 1, 2, 1, 2};

        entry.setWeights(ArrayUtils.addAll(alternateDoublingForPrefix, entry.getWeights()));
        validationResult.setCheckPassed(modulusChecker.check(SubstitutionBankDetails, entry.getWeights(), entry.getExpectedRemainder()));
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
            validationResult.setComplete(true);
        }
        return validationResult;
    }
}
