package bankdetailsvalidator.quarkus.models;

import java.util.Arrays;

/**
 * Stores the information loaded from a row in the Modulus Weight Table file.
 *
 * @author Michael Josephson
 * @see <a href="Modulus weight table data">http://www.voca.com/NR/rdonlyres/50FC0DC1-DD03-42C0-B484-1EA95849DB27/0/AccountModulus_Weight_Table.txt</a>
 */
public class ModulusWeightTableEntry {

    private int rangeStart;

    private int rangeEnd;

    private String algorithm;

    private int[] weights;

    private int expectedRemainder = 0;

    private int exceptionRule;

    private VocaDigitNumberMap vocaDigitNumberMap = VocaDigitNumberMap.getInstance();

    public ModulusWeightTableEntry(int rangeStart, int rangeEnd, String algorithm, int[] weights, int exceptionRule) {
        super();
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.algorithm = algorithm;
        this.weights = weights;
        this.exceptionRule = exceptionRule;
    }

    public ModulusWeightTableEntry(ModulusWeightTableEntry modulusWeightTableEntry) {
        super();
        this.rangeStart = modulusWeightTableEntry.getRangeStart();
        this.rangeEnd = modulusWeightTableEntry.getRangeEnd();
        this.algorithm = modulusWeightTableEntry.getAlgorithm();
        this.weights = modulusWeightTableEntry.getWeights();
        this.exceptionRule = modulusWeightTableEntry.getExceptionRule();
        this.expectedRemainder = modulusWeightTableEntry.getExpectedRemainder();
        this.vocaDigitNumberMap = modulusWeightTableEntry.getVocaDigitNumberMap();
    }

     /**
     * Constructs a <code>ModulusWeightTableEntry</code> using a set of String fields (e.g. as read from a file)
     *
     * @param fields
     */
    public ModulusWeightTableEntry(String[] fields) {
        if (fields.length < 17 || fields.length > 18) {
            throw new IllegalArgumentException("Length of fields must be 17 or 18 but was: " + fields.length);
        }
        rangeStart = Integer.parseInt(fields[0]);
        rangeEnd = Integer.parseInt(fields[1]);
        algorithm = fields[2];
        weights = new int[14];
        for (int i = 0; i < 14; i++) {
            weights[i] = Integer.parseInt(fields[i + 3]);
        }
        exceptionRule = 0;
        if (fields.length == 18) {
            exceptionRule = Integer.parseInt(fields[17]);
        }
    }

    /**
     * @return true if the account details should be considered invalid if the check indicated by this entry fails. i.e. any subsequent checks should not be
     * performed.
     */
    private boolean failOnFirstFailure() {
        switch (exceptionRule) {
            case 2:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return false;
            default:
                return true;
        }
    }

    public void setWeights(int[] weights) {
        this.weights = weights;
    }

    /**
     * Sets the weighting in the specified range of positions to 0. The positions are specified using the convention of referring to the digit positions in the
     * sorting code as u - z and the digits in the account number as a - h (as used in the Voca documentation.)
     *
     * @param startPosition
     * @param endPosition
     */
    public void zeroise(char startPosition, char endPosition) {
        final int startPositionIndex = vocaDigitNumberMap.letterToIndex(startPosition);
        final int endPositionIndex = vocaDigitNumberMap.letterToIndex(endPosition);
        for (int i = startPositionIndex; i <= endPositionIndex; i++) {
            weights[i] = 0;
        }
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getExceptionRule() {
        return exceptionRule;
    }

    private int getRangeEnd() {
        return rangeEnd;
    }

    private int getRangeStart() {
        return rangeStart;
    }

    public int getExpectedRemainder() {
        return expectedRemainder;
    }

    public void setExpectedRemainder(int expectedRemainder) {
        this.expectedRemainder = expectedRemainder;
    }

    public int[] getWeights() {
        return weights;
    }

    private VocaDigitNumberMap getVocaDigitNumberMap() {
        return vocaDigitNumberMap;
    }

    /**
     * @param sortCode
     * @return true if the specified sort code is between the start and end range for this ModulusWeightTableEntry inclusive.
     */
    private boolean isSortCodeInRange(int sortCode) {
        return (sortCode >= rangeStart) && (sortCode <= rangeEnd);
    }

    /**
     * @param sortCodeAsString
     * @return true if the specified sort code is between the start and end range for this ModulusWeightTableEntry inclusive.
     */
    public boolean isSortCodeInRange(String sortCodeAsString) {
        return isSortCodeInRange(Integer.parseInt(sortCodeAsString));
    }

    public String toDebugString() {
        final StringBuilder lBuf = new StringBuilder();
        lBuf.append("[");
        lBuf.append("rangeStart = ");
        lBuf.append(this.rangeStart);
        lBuf.append(" rangeEnd = ");
        lBuf.append(this.rangeEnd);
        lBuf.append(" algorithm = ");
        lBuf.append(this.algorithm);
        lBuf.append(" weights = { ");
        lBuf.append(Arrays.toString(weights));
        lBuf.append(" } ");
        lBuf.append("exceptionRule = ");
        lBuf.append(this.exceptionRule);
        lBuf.append("]");
        return lBuf.toString();
    }

    /**
     * @return true if the check specified by this entry is sufficient to validateSortCode the account details. i.e. any further matching entries in the modulus weight
     * table data can be skipped.
     */
    private boolean remainingEntriesShouldBeSkipped() {
        switch (exceptionRule) {
            case 2:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return true;
            default:
                return false;
        }
    }

    public boolean shouldSkipRemainingExceptions(boolean checkPassed){
        if (checkPassed) {
            return remainingEntriesShouldBeSkipped();
        }
        return failOnFirstFailure();
    }
}
