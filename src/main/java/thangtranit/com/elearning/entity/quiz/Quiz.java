package thangtranit.com.elearning.entity.quiz;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "quiz_id")
    String id;

    @Column(name = "quiz_name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;

    @Builder.Default
    @Column(name = "created_time", nullable = false)
    LocalDateTime createdTime = LocalDateTime.now();

    @Column(name = "duration", nullable = false)
    int duration;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Question> questions;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<AnswerQuiz> answerQuizList;
    public void setQuestions(List<Question> questions){
        this.questions = questions;
        questions.forEach(question -> {
            question.setQuiz(this);
            question.getAnswers().forEach(answer ->
                    answer.setQuestion(question));
        });
    }
}
