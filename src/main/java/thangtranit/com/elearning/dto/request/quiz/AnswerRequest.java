package thangtranit.com.elearning.dto.request.quiz;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.validator.ForbiddenWords;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerRequest {
    @ForbiddenWords
    @NotEmpty(message = "ANSWER_CAN_NOT_BE_EMPTY")
    String text;
    Boolean isCorrect;
}
