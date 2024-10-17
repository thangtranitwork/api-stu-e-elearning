package thangtranit.com.elearning.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ForbiddenWordsValidator.class) // Liên kết với validator logic
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Áp dụng cho trường và tham số
@Retention(RetentionPolicy.RUNTIME) // Lưu annotation ở thời gian runtime
public @interface ForbiddenWords {

    String message() default "FORBIDDEN_WORD"; // Thông báo mặc định

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] words() default {}; // Danh sách từ cấm truyền vào
}

