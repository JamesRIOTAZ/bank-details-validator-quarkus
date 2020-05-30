package bankdetailsvalidator.quarkus.models;

import org.apache.commons.lang3.StringUtils;

public class SimpleBankDetailsModel {

    private String sortCode;

    private String accountNumber;

    private VocaDigitNumberMap vocaDigitNumberMap = VocaDigitNumberMap.getInstance();

    /**
     * Constructs a <code>SimpleBankDetailsModel</code> for the account number and sort code specified.
     *
     * @param accountNumber
     * @param sortCode
     */
    public SimpleBankDetailsModel(String accountNumber, String sortCode) {
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "account '" + accountNumber + "' sort code '" + sortCode + "'";
    }

    public String getSortCodeAndAccountNumber(){
        return getSortCode()+getAccountNumber();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean isAccountNumberBlank(){
        return StringUtils.isBlank(getAccountNumber());
    }

    public String getSortCode() {
        return sortCode;
    }

    /**
     * Constructs one String consisting of the sort code followed by the account number and then returns the substring specified by the range startPosition to
     * endPosition inclusive. The positions are specified using the convention of referring to the digit positions in the sorting code as u - z and the digits
     * in the account number as a - h (as used in the Voca documentation.)
     */
    public String substring(char startPosition, char endPosition) {
        final String data = sortCode + accountNumber;
        final int startIndex = vocaDigitNumberMap.letterToIndex(startPosition);
        final int endIndex = vocaDigitNumberMap.letterToIndex(endPosition);
        return data.substring(startIndex, endIndex + 1);
    }

    /**
     * Constructs one String consisting of the sort code followed by the account number and then returns the character at the specified position. The position
     * is specified using the convention of referring to the digit positions in the sorting code as u - z and the digits in the account number as a - h (as used
     * in the Voca documentation.)
     */
    public char charAt(char position) {
        final String data = sortCode + accountNumber;
        final int index = vocaDigitNumberMap.letterToIndex(position);
        return data.charAt(index);
    }

    /**
     * Constructs one String consisting of the sort code followed by the account number and then returns the parsed int of the character at the specified
     * position. The position is specified using the convention of referring to the digit positions in the sorting code as u - z and the digits in the account
     * number as a - h (as used in the Voca documentation.)
     */
    public int intAt(char position) {
        final String data = sortCode + accountNumber;
        final int index = vocaDigitNumberMap.letterToIndex(position);
        return Integer.parseInt(data.substring(index, index + 1));
    }
}