package thangtranit.com.elearning.entity.quiz;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "answer_question_id")
    String id;

    @ManyToOne
    @JoinColumn(name = "answer_quiz_id", nullable = false)
    @ToString.Exclude
    AnswerQuiz answerQuiz;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    Question question;

    @ManyToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "answer_id", nullable = false)
    Answer answer;
}

