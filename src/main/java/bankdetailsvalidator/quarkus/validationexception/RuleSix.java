package bankdetailsvalidator.quarkus.validationexception;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleSix implements Rule {

    /*
     * If a = 4, 5, 6, 7 or 8, and g and h are the same, the accounts are for a foreign currency and the checks cannot be used.
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {
        if (ArrayUtils.indexOf(new char[]{'4', '5', '6', '7', '8'}, bankDetails.charAt('a')) != -1
                && bankDetails.charAt('g') == bankDetails.charAt('h')) {
            validationResult.setCheckPassed(true);
        }else {
            validationResult.setCheckPassed(modulusChecker.check(bankDetails.getSortCodeAndAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
            validationResult.updateMessage(modulusChecker.getName());
            if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
                validationResult.setComplete(true);
            }
        }
        return validationResult;
    }
}
