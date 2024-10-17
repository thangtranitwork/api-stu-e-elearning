package thangtranit.com.elearning.dto.request.quiz;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.validator.ForbiddenWords;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizRequest {
    @ForbiddenWords
    @Size(min = 5, max = 255, message = "QUIZ_NAME_LENGTH_IS_INVALID")
    String name;
    @ForbiddenWords
    @Size(min = 5, max = 255, message = "QUIZ_DESCRIPTION_LENGTH_IS_INVALID")
    String description;
    @Min(value = 1, message = "QUIZ_DURATION__IS_INVALID")
    int duration;
    @Size(min = 2, message = "QUESTION_LIST_SIZE_IS_INVALID")
    List<QuestionRequest> questions;
}
