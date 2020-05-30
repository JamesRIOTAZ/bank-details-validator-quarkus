package bankdetailsvalidator.quarkus.api.impl;


import bankdetailsvalidator.quarkus.api.BankDetailsValidator;
import bankdetailsvalidator.quarkus.models.BankDetailsValidationModel;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;
import bankdetailsvalidator.quarkus.models.SimpleBankDetailsModel;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;
import bankdetailsvalidator.quarkus.supplier.ModulusCheckerMapSupplier;
import bankdetailsvalidator.quarkus.supplier.ModulusWeightTableSupplier;
import bankdetailsvalidator.quarkus.supplier.SortCodeSubstitutionTableSupplier;
import bankdetailsvalidator.quarkus.validationexception.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements an in-house bank details validator for customers who do not want to use a 3rd party service. Uses the algorithms and data provided at <a
 * href="http://www.vocalink.com/products/payments/customer-support-services/modulus-checking.aspx">Voca Modulus checking</a>.
 * This introduces a dependency on two files which are occasionally updated:
 * <ul>
 * <li><a href = "http://www.vocalink.com/products/payments/customer-support-services/modulus-checking.aspx">Modulus checking</a></li>
 * <li><a href="http://www.vocalink.com/media/713846/valacdos.txt">Current Modulus weight table data</a></li>
 * <li><a href="http://www.vocalink.com/media/300584/scsubtab.txt">Current Sorting code substitution data</a></li>
 * </ul>
 * You can register to receive an email notification when a new version of the specification and data files are available at <a
 * href="http://www.vocalink.com/contact/subscribe.aspx">this page</a>.
 */
@Service
public class CDLBankDetailsValidator implements BankDetailsValidator {

    private final ValidationExceptionRuleFactory validationExceptionRuleFactory;

    /**
     * List of the module weight table entries.
     */
    private final List<ModulusWeightTableEntry> modulusWeightTable = new ArrayList<>();

    /**
     * Maps sort codes to their substitution values.
     */
    private final Map<String, String> sortCodeSubstitutionTable = new HashMap<>();

    /**
     * Map of names of modulus check algorithms to their implementations.
     */
    private final Map<String, WeightedModulusChecker> modulusCheckersByAlgorithmName = new HashMap<>();

    private static final String SORT_CODE_NOT_RECOGNISED = "Sort code not recognised in Modulus Weight Table";

    @Autowired
    public CDLBankDetailsValidator(ModulusWeightTableSupplier modulusWeightTableSupplier,
                                   SortCodeSubstitutionTableSupplier sortCodeSubstitutionTableSupplier,
                                   ModulusCheckerMapSupplier modulusCheckerMapSupplier,
                                   ValidationExceptionRuleFactory validationExceptionRuleFactory) {
        this.validationExceptionRuleFactory = validationExceptionRuleFactory;
        modulusCheckersByAlgorithmName.putAll(modulusCheckerMapSupplier.get());
        modulusWeightTable.addAll(modulusWeightTableSupplier.get());
        sortCodeSubstitutionTable.putAll(sortCodeSubstitutionTableSupplier.get());
    }

    @Override
    public BankDetailsValidationModel validateSortCode(String sortCode){
        final ModulusWeightTableEntry[] mwtEntries = getCopiesOfModulusWeightTableEntriesToUse(sortCode);
        if(noEntriesFound(mwtEntries)){
            return new BankDetailsValidationModel(false, false, SORT_CODE_NOT_RECOGNISED, "");
        }
        return new BankDetailsValidationModel(false, true, "Sort code recognised in Modulus Weight Table", "");
    }

    @Override
    public BankDetailsValidationModel validateAccount(String accountNumber, String sortCode) {
        final SimpleBankDetailsModel bankDetails = new SimpleBankDetailsModel(accountNumber, sortCode);

        final ModulusWeightTableEntry[] mwtEntries = getCopiesOfModulusWeightTableEntriesToUse(sortCode);
        if(noEntriesFound(mwtEntries)){
            return new BankDetailsValidationModel(true, false, "", "");
        }
        if(accountNumberNotFound(bankDetails)){
            return new BankDetailsValidationModel(false, true, "No Account Number Provided", "");
        }

        ValidationResult validationResult = createValidationResultModel(mwtEntries, bankDetails);
        return new BankDetailsValidationModel(validationResult.isCheckPassed(), true, validationResult.getMessage(), "");
    }

    private ValidationResult createValidationResultModel(ModulusWeightTableEntry[] mwtEntries, SimpleBankDetailsModel bankDetails){
        ValidationResult validationResult = new ValidationResult();

        for (final ModulusWeightTableEntry entry : mwtEntries) {
            final WeightedModulusChecker modulusChecker = modulusCheckersByAlgorithmName.get(entry.getAlgorithm());
            final Rule rule = getExceptionRule(entry.getExceptionRule());

            validationResult = rule.process(entry, modulusChecker, validationResult, bankDetails, sortCodeSubstitutionTable);
            if(validationResult.isComplete()){
                break;
            }
        }
        return validationResult;
    }

    private boolean accountNumberNotFound(SimpleBankDetailsModel bankDetails) {
        return bankDetails.isAccountNumberBlank();
    }

    private boolean noEntriesFound(ModulusWeightTableEntry[] mwtEntries) {
        if(mwtEntries.length <= 0){
            return true;
        }
        return false;
    }

    /**
     * Search the list of ModulusWeightTableEntries that have been loaded from the file and select the appropriate one to use for the specified sort code. Some
     * of the exception rules specify that weights are modified so getCopiesOfModulusWeightTableEntriesToUse returns copies of the table entries that can be
     * safely modified.
     *
     * @param sortCodeAsString sort code to use for looking up entries.
     */
    private ModulusWeightTableEntry[] getCopiesOfModulusWeightTableEntriesToUse(String sortCodeAsString){
        final List<ModulusWeightTableEntry> list = new ArrayList<>();
        for (final ModulusWeightTableEntry mwte : modulusWeightTable) {
            if (mwte.isSortCodeInRange(sortCodeAsString)) {
                list.add(new ModulusWeightTableEntry(mwte));
            }
        }
        return list.toArray(new ModulusWeightTableEntry[0]);
    }

    private Rule getExceptionRule(int exceptionRule) {
        return validationExceptionRuleFactory.getValidationExceptionRule(exceptionRule);
    }
}
