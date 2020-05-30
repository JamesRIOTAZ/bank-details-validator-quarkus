package bankdetailsvalidator.quarkus.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static bankdetailsvalidator.quarkus.models.SortCodeModel.SORT_CODE_LENGTH;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Pattern(regexp = "^[0-9]*$", message="Sort code {javax.validation.constraints.Pattern.invalid.message}")
@Size(max= SORT_CODE_LENGTH, min= SORT_CODE_LENGTH, message="Sort code {javax.validation.constraints.Size.length.message}")
@NotEmpty(message = "Missing sort code")
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface ValidSortCode {

    String message() default "Invalid Sort Code";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}