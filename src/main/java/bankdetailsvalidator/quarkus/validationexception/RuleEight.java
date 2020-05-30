package bankdetailsvalidator.quarkus.validationexception;

import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RuleEight implements Rule {

    /*
     * Perform the check as specified, except substitute the sorting code with 090126, for check purposes only.
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {

        final String SubstitutionBankDetails = "090126"+bankDetails.getAccountNumber();

        validationResult.setCheckPassed(modulusChecker.check(SubstitutionBankDetails, entry.getWeights(), entry.getExpectedRemainder()));
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
            validationResult.setComplete(true);
        }
        return validationResult;
    }




}
