package thangtranit.com.elearning.dto.request.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerQuestionRequest {
    String questionId;
    String answerId;
}
