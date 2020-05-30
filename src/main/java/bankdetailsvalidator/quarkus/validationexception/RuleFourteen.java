package bankdetailsvalidator.quarkus.validationexception;

import org.springframework.stereotype.Component;
import bankdetailsvalidator.quarkus.api.impl.ValidationResult;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;

import java.util.Map;

@Component
public class RuleFourteen implements Rule {

     /*
     * If the check passes (that is, there is no remainder), then the account number should be considered
     * valid. Do not perform the second check
     * If the first check fails, then the second check [with modified account number as below] must be performed
     */

    @Override
    public ValidationResult process(ModulusWeightTableEntry entry, WeightedModulusChecker modulusChecker, ValidationResult validationResult, SimpleBankDetailsModel bankDetails, Map<String, String> sortCodeSubstitutionTable) {
        validationResult.setCheckPassed(modulusChecker.check(bankDetails.getSortCodeAndAccountNumber(), entry.getWeights(), entry.getExpectedRemainder()));
        if (!validationResult.isCheckPassed()) {
            /*
             * If the 8th digit [of the account number] is 0, 1 or 9, then remove the digit from the
             * account number and insert a 0 as the 1st digit for check purposes only
             */
            final char digit8 = bankDetails.charAt('h');
            if ('0' == digit8 || '1' == digit8 || '9' == digit8) {
                String substitutionSortCode = bankDetails.getSortCode() + "0";
                String substitutionAccountNumber = new StringBuilder(bankDetails.getAccountNumber()).deleteCharAt(7).toString();

                 /*
                 * Perform the modulus 11 check on the modified account number using the same weightings as
                 * specified in the table (that is, 0 0 0 0 0 0 8 7 6 5 4 3 2 1):
                 * - If there is no remainder, then the account number should be considered valid
                 * - If there is a remainder, then the account number fails the second check and is not a valid Coutts
                 * account number.
                 */
                validationResult.setCheckPassed(modulusChecker.check(substitutionSortCode+substitutionAccountNumber, entry.getWeights(), entry.getExpectedRemainder()));
            }
            /*
             * If the 8th digit of the account number (reading from left to right) is not 0, 1 or 9 then the account
             * number fails the second check and is not a valid Coutts account number
             */
        }
        validationResult.updateMessage(modulusChecker.getName());
        if (entry.shouldSkipRemainingExceptions(validationResult.isCheckPassed())) {
            validationResult.setComplete(true);
        }
        return validationResult;
    }
}
