package thangtranit.com.elearning.dto.response.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartAnswerQuizResponse {
    String id;
    String name;
    int duration;
    long remainingTime;
    List<QuestionResponse> questions;
}
