package bankdetailsvalidator.quarkus.controller;

import bankdetailsvalidator.quarkus.api.BankDetailsValidator;
import bankdetailsvalidator.quarkus.api.impl.CDLBankDetailsValidator;
import bankdetailsvalidator.quarkus.api.impl.ValidationExceptionRuleFactory;
import bankdetailsvalidator.quarkus.models.AccountModel;
import bankdetailsvalidator.quarkus.moduluschecker.DoubleAlternateModulusChecker;
import bankdetailsvalidator.quarkus.supplier.ModulusCheckerMapSupplier;
import bankdetailsvalidator.quarkus.supplier.ModulusWeightTableSupplier;
import bankdetailsvalidator.quarkus.supplier.SortCodeSubstitutionTableSupplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bankdetailsvalidator.quarkus.models.BankDetailsValidationModel;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "Bank Details Services API")
@RequestMapping("/bank-details-validation/v1")
public class BankDetailsValidatorController {

    private HttpStatus status;

    @PostMapping(path = "account-validation", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BankDetailsValidationModel> validate(@RequestBody @Valid AccountModel accountModel) {

        BankDetailsValidator bankDetailsValidator = new CDLBankDetailsValidator(
                new ModulusWeightTableSupplier(),
                new SortCodeSubstitutionTableSupplier(),
                new ModulusCheckerMapSupplier(new DoubleAlternateModulusChecker()),
                new ValidationExceptionRuleFactory()
        );

        final BankDetailsValidationModel validationResult = bankDetailsValidator.validateAccount(accountModel.getAccountNumber(), accountModel.getSortCode());
        ResponseEntity response = getResponseEntity(!validationResult.isValid(), validationResult);
        return response;
    }

    private ResponseEntity<BankDetailsValidationModel> getResponseEntity(boolean unprocessable, BankDetailsValidationModel validationResult){
        if (unprocessable) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
            return ResponseEntity.status(status).body(validationResult);
        } else{
            status = HttpStatus.OK;
            return ResponseEntity.ok(validationResult);
        }
    }
}