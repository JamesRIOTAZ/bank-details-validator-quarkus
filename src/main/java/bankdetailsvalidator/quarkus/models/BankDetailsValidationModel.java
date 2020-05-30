package bankdetailsvalidator.quarkus.models;

import com.google.common.base.Objects;

import java.io.Serializable;

public class BankDetailsValidationModel implements Serializable {

    /** The Constant serialVersionUID. */
    static final long serialVersionUID = -6438714972899174255L;

    /** The valid. */
    private boolean valid;

    /** The sort code recognised. */
    private boolean sortCodeRecognised;

    /** The validation error description. */
    private String validationErrorDescription;

    /** The validation error code. */
    private final String validationErrorCode;

    /**
     * Instantiates a new bank details validation model.
     *
     * @param valid
     *            the valid
     * @param sortCodeRecognised
     *            the sort code recognised
     * @param validationErrorDescription
     *            the validation error description
     * @param validationErrorCode
     *            the validation error code
     */
    public BankDetailsValidationModel(boolean valid, boolean sortCodeRecognised, String validationErrorDescription, String validationErrorCode) {
        this.valid = valid;
        this.sortCodeRecognised = sortCodeRecognised;
        this.validationErrorDescription = validationErrorDescription;
        this.validationErrorCode = validationErrorCode;
    }

    /**
     * Checks if is valid.
     *
     * @return true, if is valid
     */
    public boolean isValid() {
        return valid;
    }

    public boolean isSortCodeRecognised() {
        return sortCodeRecognised;
    }

    /**
     * Gets the validation error code.
     *
     * @return the validation error code
     */
    public String getValidationErrorCode() {
        return validationErrorCode;
    }

    /**
     *
     * Gets the validation error description.
     *
     * @return the validation error description
     */
    public String getValidationErrorDescription() {
        return validationErrorDescription.trim();
    }

    @Override
    public boolean equals(Object pObject) {

        if (pObject == null) {
            return false;
        }

        if (!pObject.getClass().equals(this.getClass())) {
            return false;
        }

        BankDetailsValidationModel compareTo =(BankDetailsValidationModel)pObject;

        return compareTo.getValidationErrorCode().equals(validationErrorCode) &&
                compareTo.getValidationErrorDescription().equals(validationErrorDescription) &&
                compareTo.isValid() == valid && compareTo.isSortCodeRecognised() == sortCodeRecognised;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.valid, this.sortCodeRecognised, this.validationErrorCode, this.validationErrorDescription);
    }

    @Override
    public String toString() {
        return "  \nBankDetailsValidationModel{" +
                "  \nvalid=" + valid +
                "    \nsortCodeRecognised=" + sortCodeRecognised +
                "    \nvalidationErrorDescription='" + validationErrorDescription + '\'' +
                "    \nvalidationErrorCode='" + validationErrorCode + '\'' +
                '}';
    }

    public String toSingleLineString(){
        return "valid="+valid+", "+
                "sortCodeRecognised="+sortCodeRecognised+", "+
                "validationErrorDescription="+validationErrorDescription+", "+
                "validationErrorCode="+ validationErrorCode;
    }
}
