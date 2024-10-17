package thangtranit.com.elearning.entity.quiz;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "answer_quiz")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "answer_quiz_id")
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    Quiz quiz;

    @Builder.Default
    @Column(name = "answer_time", nullable = false)
    LocalDateTime answerTime = LocalDateTime.now();

    @Column(name = "time_out", nullable = false)
    LocalDateTime timeOut;

    @Builder.Default
    @Column(name = "score")
    int score = 0;

    @OneToMany(mappedBy = "answerQuiz", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<AnswerQuestion> answerQuestions;

    @Builder.Default
    @Column(name = "is_complete")
    boolean isComplete = false;

    public void setAnswerQuestions(List<AnswerQuestion> answerQuestions){
        this.answerQuestions = answerQuestions;
        answerQuestions.forEach(answerQuestion -> answerQuestion.setAnswerQuiz(this));
    }

    public long getRemainingTime(){
        Duration duration = Duration.between(answerTime, LocalDateTime.now());
        long remainingTime = quiz.getDuration() * 60L - duration.getSeconds();
        if (remainingTime < 0) remainingTime = 0;
        return remainingTime;
    }

}
