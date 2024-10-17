package thangtranit.com.elearning.dto.request.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.validator.ForbiddenWords;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRequest {
    @ForbiddenWords
    @Size(min = 5, max = 255, message = "COURSE_NAME_LENGTH_IS_INVALID")
    String name;
    @ForbiddenWords
    @Size(min = 5, max = 255, message = "COURSE_DESCRIPTION_LENGTH_IS_INVALID")
    String description;
    @ForbiddenWords
    @Size(min = 100, message = "COURSE_INTRODUCTION_LENGTH_IS_INVALID")
    String introduction;
    @Builder.Default
    @Min(value = 0, message = "COURSE_PRICE_IS_INVALID")
    int price = 0;
}
