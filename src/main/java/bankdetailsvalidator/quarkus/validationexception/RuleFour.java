package bankdetailsvalidator.quarkus.validationexception;

import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleFour implements Rule {

    /*
     * Ensure that the remainder is the same as the two-digit checkdigit the checkdigit for exception 4 is gh from the original account
     * number.
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {
        entry.setExpectedRemainder(Integer.parseInt(bankDetails.substring('g', 'h')));
        validationResult.setCheckPassed(modulusChecker.check(bankDetails.getSortCodeAndAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
            validationResult.setComplete(true);
        }
        return validationResult;
    }
}
