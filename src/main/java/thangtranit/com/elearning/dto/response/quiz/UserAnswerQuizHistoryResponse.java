package thangtranit.com.elearning.dto.response.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAnswerQuizHistoryResponse {
    QuizInfoResponse quiz;
    LocalDateTime answerTime;
    Boolean isComplete;
    Boolean canContinue;
    int score;
    int numberOfQuestions;
}
