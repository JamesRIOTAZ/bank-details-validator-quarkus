package bankdetailsvalidator.quarkus.moduluschecker;

public class StandardModulusChecker implements WeightedModulusChecker {

    private static final String BAD_COMBINE_MESSAGE = "Implementation of combineDigits only supports numbers in the range 0 <= n <= 99";

    private int modulus;

    public StandardModulusChecker(){
        super();
    }

    public StandardModulusChecker(int modulus) {
        this.modulus = modulus;
    }

    public boolean check(String data, int[] weights, int expectedRemainder) {
        final int remainder = getRemainder(data, weights);
        final boolean result = remainder == expectedRemainder;
        return result;
    }

    public boolean check(String data, int[] weights) {
        return check(data, weights, 0);
    }

    public String getName() {
        return "Standard " + modulus + " modulus check";
    }

    public int getRemainder(String data, int[] weights) {
        final char[] digits = onlyDigits(data);
        if (digits.length > weights.length) {
            throw new IllegalArgumentException(digits.length + " digit(s) in data but only " + weights.length + " weight(s) provided");
        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            final int product = mapToInt(digits[i]) * weights[i];
            sum = sum + product;
        }
        return sum % modulus;
    }

    /**
     * Returns an array of chars from the specified String with all non-numeric characters removed. e.g. onlyDigits("123-456") => {'1', '2', '3', '4', '5', '6'}
     *
     * @param stringWithPossibleOtherCharacters
     * @return the array of digit characters
     */
    protected char[] onlyDigits(String stringWithPossibleOtherCharacters) {
        final StringBuilder digits = new StringBuilder();
        for (int i = 0; i < stringWithPossibleOtherCharacters.length(); i++) {
            final char ch = stringWithPossibleOtherCharacters.charAt(i);
            if (Character.isDigit(ch)) {
                digits.append(ch);
            }
        }
        return digits.toString().toCharArray();
    }

    /**
     * Maps a character in the range '0' <= c <= '9' to the corresponding int 0 - 9.
     *
     * @param c
     *            the character to map
     * @return the corresponding int
     * @throws IllegalArgumentException
     *             if c is out of range
     */
    protected int mapToInt(char c) {
        if (c < '0' || c > '9') {
            throw new IllegalArgumentException("Character c must be in the range 0 <= c <= 9");
        }
        return c - '0';
    }

    /**
     * Returns the sum of the digits in <code>number</code>, for example <code>combineDigits(18) == 9</code>. This implementation only supports 1 and 2 digit
     * numbers. If the number is only 1 digit the same number is returned.
     *
     * @throws IllegalArgumentException
     *             if number is less than 0 or greater than 99.
     */
    protected int combineDigits(int number) {
        if (number < 0 || number > 99) {
            throw new IllegalArgumentException(number + " out of range (" + BAD_COMBINE_MESSAGE + ")");
        }
        final int tens = number / 10;
        final int units = number % 10;
        return tens + units;
    }
}