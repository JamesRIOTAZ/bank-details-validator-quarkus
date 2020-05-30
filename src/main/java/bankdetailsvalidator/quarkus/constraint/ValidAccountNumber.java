package bankdetailsvalidator.quarkus.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bankdetailsvalidator.quarkus.models.AccountModel.ACCOUNT_NUMBER_LENGTH;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp = "^[0-9]*$", message="Account number {javax.validation.constraints.Pattern.invalid.message}")
@Size(max= ACCOUNT_NUMBER_LENGTH, min=ACCOUNT_NUMBER_LENGTH, message="Account number {javax.validation.constraints.Size.length.message}")
@NotEmpty(message = "Missing account number")
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface ValidAccountNumber {

    String message() default "Invalid Account Number";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
