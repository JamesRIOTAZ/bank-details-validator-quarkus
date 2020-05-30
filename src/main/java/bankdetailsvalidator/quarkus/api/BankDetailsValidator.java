package bankdetailsvalidator.quarkus.api;

import bankdetailsvalidator.quarkus.models.BankDetailsValidationModel;
import org.springframework.stereotype.Component;

@Component
public interface BankDetailsValidator {

    BankDetailsValidationModel validateAccount(String accountNumber, String sortCode) ;
    BankDetailsValidationModel validateSortCode(String sortCode);
}
