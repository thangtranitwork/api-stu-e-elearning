package thangtranit.com.elearning.repository.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.quiz.AnswerQuiz;

import java.util.Optional;

@Repository
public interface AnswerQuizRepository extends JpaRepository<AnswerQuiz, String> {
    @Query("SELECT a FROM AnswerQuiz a WHERE a.quiz.id = :quizId AND a.user.id = :userId AND a.isComplete = false AND a.timeOut > CURRENT_TIMESTAMP")
    Optional<AnswerQuiz> findByQuizIdAndUserIdCanContinue(String quizId, String userId);
    Page<AnswerQuiz> findAllByUserIdOrderByAnswerTimeDesc(String userId, Pageable pageable);
}
