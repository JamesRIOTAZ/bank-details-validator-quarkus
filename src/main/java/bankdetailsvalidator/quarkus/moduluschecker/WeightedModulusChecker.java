package bankdetailsvalidator.quarkus.moduluschecker;

/**
 * Common interface for the various weighted modulus checkers. These are checkers which take some data in the form of a String of digits and an array of weights.
 * The weights indicate the amount that each digit in the data should be multiplied by.
 *
 */
public interface WeightedModulusChecker {

    /**
     * Calculates the checksum for the specified data.
     *
     * @return true if the remainder after dividing by the sum by the modulus is 0, false otherwise.
     */
    boolean check(String data, int[] weights);

    int getRemainder(String data, int[] weights);

    /**
     * Calculates the checksum for the specified data.
     *
     * @return true if the remainder after dividing by the sum by the modulus is <code>expectedRemainder</code>, false otherwise.
     */
    boolean check(String data, int[] weights, int expectedRemainder);

    /**
     * @return A descriptive name of this algorithm. (e.g. "Double alternate modulus check")
     */
    String getName();

}