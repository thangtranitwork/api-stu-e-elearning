package thangtranit.com.elearning.dto.request.course;

import jakarta.validation.constraints.Max;
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
public class ReviewRequest {
    @Min(value = 0, message = "INVALID_STAR")
    @Max(value = 5, message = "INVALID_STAR")
    byte star;
    @ForbiddenWords
    @Size(min = 5, max = 255, message = "REVIEW_LENGTH_IS_INVALID")
    String text;
}
