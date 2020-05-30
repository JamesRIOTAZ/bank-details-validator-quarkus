package bankdetailsvalidator.quarkus.models;

import org.apache.commons.lang3.ArrayUtils;

public class VocaDigitNumberMap {

    private static char[] notationToDigitNumberMap = new char[] { 'u', 'v', 'w', 'x', 'y', 'z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

    private static VocaDigitNumberMap instance = null;

    public static synchronized VocaDigitNumberMap getInstance() {
        if (instance == null) {
            instance = new VocaDigitNumberMap();
        }
        return instance;
    }

    public int letterToIndex(char letter) {
        final int number = ArrayUtils.indexOf(notationToDigitNumberMap, letter);
        if (number == -1) {
            throw new IllegalArgumentException("Invalid letter: " + letter);
        }
        return number;
    }
}
