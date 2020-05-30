package bankdetailsvalidator.quarkus.supplier;

import bankdetailsvalidator.quarkus.moduluschecker.DoubleAlternateModulusChecker;
import bankdetailsvalidator.quarkus.moduluschecker.StandardModulusChecker;
import bankdetailsvalidator.quarkus.moduluschecker.WeightedModulusChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class ModulusCheckerMapSupplier implements Supplier<Map<String, WeightedModulusChecker>> {

    private DoubleAlternateModulusChecker doubleAlternateModulusChecker;

    /**
     * String for how the MOD11 check is specified in the modulus weight table.
     */
    private static final String MOD11 = "MOD11";

    /**
     * String for how the MOD10 check is specified in the modulus weight table.
     */
    private static final String MOD10 = "MOD10";

    /**
     * String for how the DBLAL check is specified in the modulus weight table.
     */
    private static final String DBLAL = "DBLAL";

    @Autowired
    public ModulusCheckerMapSupplier(DoubleAlternateModulusChecker doubleAlternateModulusChecker){
        this.doubleAlternateModulusChecker = doubleAlternateModulusChecker;
    }

    @Override
    public Map<String, WeightedModulusChecker> get() {

        Map<String, WeightedModulusChecker> modulusCheckersByAlgorithmName = new HashMap<>();
        modulusCheckersByAlgorithmName.put(DBLAL, doubleAlternateModulusChecker);
        modulusCheckersByAlgorithmName.put(MOD10, new StandardModulusChecker(10));
        modulusCheckersByAlgorithmName.put(MOD11, new StandardModulusChecker(11));
        return modulusCheckersByAlgorithmName;
    }
}
