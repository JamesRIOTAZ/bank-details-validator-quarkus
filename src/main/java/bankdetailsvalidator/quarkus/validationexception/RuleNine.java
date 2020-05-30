package bankdetailsvalidator.quarkus.validationexception;

import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleNine implements Rule {

    /*
     * All LTSB euro accounts are held at sorting code 30-96-34, however customers may perceive that their euro account is held at the
     * branch where sterling accounts are held and thus quote a sorting code other than 30-96-34. The combination of the "sterling"
     * sorting code and "euro" account number will cause the first standard modulus 11 check to fail. In such cases, carry out the
     * second modulus 11 check, substituting the sorting code with 309634 and the appropriate weighting. If this check passes it is
     * deemed to be a valid euro account.
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {
        validationResult.setCheckPassed(modulusChecker.check(bankDetails.getSortCodeAndAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
        if (!validationResult.isCheckPassed()) {
            String substitutionSortCode = "309634";
            validationResult.setCheckPassed(modulusChecker.check(substitutionSortCode+bankDetails.getAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
        }
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())){
            validationResult.setComplete(true);
        }
        return validationResult;
    }
}
