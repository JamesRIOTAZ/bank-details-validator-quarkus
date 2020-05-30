package bankdetailsvalidator.quarkus.models;

public class AccountModel {
    public static final int ACCOUNT_NUMBER_LENGTH = 8;

    private String accountNumber;

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
