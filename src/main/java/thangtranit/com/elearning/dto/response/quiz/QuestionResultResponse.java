package thangtranit.com.elearning.dto.response.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionResultResponse {
    String id;
    String correctId;
    String chosenId;
}
