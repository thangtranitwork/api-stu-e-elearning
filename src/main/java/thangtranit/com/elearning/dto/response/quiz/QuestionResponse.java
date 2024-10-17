package thangtranit.com.elearning.dto.response.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResponse {
    String id;
    String text;
    List<AnswerResponse> answers;
}
