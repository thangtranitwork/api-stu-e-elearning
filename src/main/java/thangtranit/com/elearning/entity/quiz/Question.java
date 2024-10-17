package thangtranit.com.elearning.entity.quiz;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "question_id")
    String id;

    @Column(name = "question_text", nullable = false)
    String text;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    @ToString.Exclude
    Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Answer> answers;

    public Answer getCorrectAnswer() {
        return answers.stream()
                .filter(Answer::isCorrect)
                .findFirst().orElseThrow(()->new AppException(ErrorCode.QUESTION_MUST_HAVE_ONE_CORRECT_ANSWER));
    }
}
