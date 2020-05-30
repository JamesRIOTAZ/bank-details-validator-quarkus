package bankdetailsvalidator.quarkus;

import bankdetailsvalidator.quarkus.models.AccountModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bankdetailsvalidator.quarkus.models.BankDetailsValidationModel;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "Bank Details Services API")
@RequestMapping("/bank-details-validation/v1")
public class ExampleResource {

    private HttpStatus status;

    @PostMapping(path = "account-validation", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BankDetailsValidationModel> validate(@RequestBody AccountModel accountModel) {
        final BankDetailsValidationModel validationResult =
                new BankDetailsValidationModel(true, true, "passed", "passed");
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