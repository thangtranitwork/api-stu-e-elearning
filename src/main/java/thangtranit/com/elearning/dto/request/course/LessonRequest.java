package thangtranit.com.elearning.dto.request.course;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.validator.ForbiddenWords;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonRequest {
    @ForbiddenWords
    @Size(min = 5, max = 255, message = "LESSON_NAME_LENGTH_IS_INVALID")
    String name;
    @ForbiddenWords
    @Size(min = 5, message = "LESSON_THEORY_LENGTH_IS_INVALID")
    String theory;
}
