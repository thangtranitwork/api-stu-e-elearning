package thangtranit.com.elearning.dto.request.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmitQuizRequest {
    List<AnswerQuestionRequest> answers;
}
