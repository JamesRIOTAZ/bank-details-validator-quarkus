package bankdetailsvalidator.quarkus.validationexception;

import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleTwo implements Rule {

    /*
     * Only occurs for some standard modulus 11 checks, when there is a 2 in the exception column for the first check for a sorting code and
     * a 9 in the exception column for the second check for the same sorting code. This is used specifically for LTSB euro accounts.
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {
        if (bankDetails.charAt('a') != '0' && bankDetails.charAt('g') != '9') {
            entry.setWeights(new int[]{0, 0, 1, 2, 5, 3, 6, 4, 8, 7, 10, 9, 3, 1});
        } else if (bankDetails.charAt('a') != '0' && bankDetails.charAt('g') == '9') {
            entry.setWeights(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 8, 7, 10, 9, 3, 1});
        }
        validationResult.setCheckPassed(modulusChecker.check(bankDetails.getSortCodeAndAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
            validationResult.setComplete(true);
        }
        return validationResult;
    }
}
