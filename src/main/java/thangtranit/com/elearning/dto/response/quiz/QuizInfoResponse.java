package thangtranit.com.elearning.dto.response.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizInfoResponse {
    String id;
    String name;
    String description;
    LocalDateTime createdTime;
    UserMinimumInfoResponse user;
    int duration;
    int numberOfQuestions;
    int playedTimes;
}
