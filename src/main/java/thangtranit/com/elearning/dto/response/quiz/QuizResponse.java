package thangtranit.com.elearning.dto.response.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResponse {
    String id;
    String name;
    String description;
    LocalDateTime createdTime;
    UserMinimumInfoResponse user;
    int duration;
    List<QuestionResponse> questions;

    public List<QuestionResponse> getQuestions() {
        questions.forEach(question -> Collections.shuffle(question.getAnswers()));
        Collections.shuffle(questions);
        return questions;
    }
}