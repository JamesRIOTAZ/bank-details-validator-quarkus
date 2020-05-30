package bankdetailsvalidator.quarkus.validationexception;

import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleThree implements Rule {

    /**
     * String for how the DBLAL check is specified in the modulus weight table.
     */
    private static final String DBLAL = "DBLAL";

    // If c = 6 or 9 the double alternate check does not need to be carried out.

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {
        if (!(bankDetails.charAt('c') == '6' || bankDetails.charAt('c') == '9') && entry.getAlgorithm().equals(DBLAL)) {
            validationResult.setCheckPassed(modulusChecker.check(bankDetails.getSortCodeAndAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
            validationResult.updateMessage(modulusChecker.getName());
            if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
                validationResult.setComplete(true);
            }
        }
        return validationResult;
    }
}
