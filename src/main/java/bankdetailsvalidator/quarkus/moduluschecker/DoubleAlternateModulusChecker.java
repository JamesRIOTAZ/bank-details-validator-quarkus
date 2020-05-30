package bankdetailsvalidator.quarkus.moduluschecker;

import org.springframework.stereotype.Component;

@Component
public class DoubleAlternateModulusChecker extends StandardModulusChecker {

    @Override
    public String getName() {
        return "Double alternate modulus check";
    }

    @Override
    public int getRemainder(String data, int[] weights) {
        final char[] digits = onlyDigits(data);
        if (digits.length != weights.length) {
            throw new IllegalArgumentException(digits.length + " digit(s) in data: '" + data + "' but " + weights.length + " weight(s) provided");

        }
        int sum = 0;
        for (int i = 0; i < digits.length; i++) {
            final int product = mapToInt(digits[i]) * weights[i];
            final int combined = combineDigits(product);
            sum = sum + combined;
        }
        return sum % 10;
    }
}
