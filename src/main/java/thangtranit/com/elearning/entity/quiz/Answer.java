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
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "answer_id")
    String id;

    @Column(name = "answer_text", nullable = false)
    String text;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id", nullable = false)
    Question question;

    @Column(name = "is_correct", nullable = false)
    boolean isCorrect;
}
