package bankdetailsvalidator.quarkus.validationexception;

import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleTen implements Rule {

    /*
     * These exceptions are for some LTSB accounts. If there is a 10 in the exception column for the first check for a sorting code and an
     * 11 in the exception column for the second check for the same sorting code, if either check is successful the account number is deemed
     * valid. For the exception 10 check, if ab = 09 or 99 and g=9, zeroise weighting positions u-b.
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {
        if ((bankDetails.substring('a', 'b').equals("09") || bankDetails.substring('a', 'b').equals("99")) && bankDetails.charAt('g') == '9') {
            entry.zeroise('u', 'b');
        }
        validationResult.setCheckPassed(modulusChecker.check(bankDetails.getSortCodeAndAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
            validationResult.setComplete(true);
        }
        return validationResult;
    }
}
