package bankdetailsvalidator.quarkus.api.impl;

import bankdetailsvalidator.quarkus.validationexception.*;
import org.springframework.stereotype.Component;

@Component
public class ValidationExceptionRuleFactory {

    public Rule getValidationExceptionRule(int exceptionRule){

        switch (exceptionRule) {
            case 1: return new RuleOne();
            case 2: return new RuleTwo();
            case 3: return new RuleThree();
            case 4: return new RuleFour();
            case 5: return new RuleFive();
            case 6: return new RuleSix();
            case 7: return new RuleSeven();
            case 8: return new RuleEight();
            case 9: return new RuleNine();
            case 10: return new RuleTen();
            case 14: return new RuleFourteen();
            default: return new RuleDefault();
        }
    }
}
