package bankdetailsvalidator.quarkus.models;

import bankdetailsvalidator.quarkus.constraint.ValidAccountNumber;
import bankdetailsvalidator.quarkus.constraint.ValidSortCode;

public class AccountModel {
    public static final int ACCOUNT_NUMBER_LENGTH = 8;

    @ValidAccountNumber
    private String accountNumber;

    @ValidSortCode
    private String sortCode;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
}
