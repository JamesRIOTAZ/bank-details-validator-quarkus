package bankdetailsvalidator.quarkus.models;

import bankdetailsvalidator.quarkus.constraint.ValidSortCode;

public class SortCodeModel {

    public static final int SORT_CODE_LENGTH = 6;

    @ValidSortCode
    private String sortCode;

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
}
