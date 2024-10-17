package thangtranit.com.elearning.dto.request.quiz;

import jakarta.validation.constraints.NotEmpty;
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
public class QuestionRequest {
    @ForbiddenWords
    @NotEmpty(message = "QUESTION_CAN_NOT_BE_EMPTY")
    String text;
    @Size(min = 2, message = "ANSWER_LIST_SIZE_IS_INVALID")
    List<AnswerRequest> answers;
}
