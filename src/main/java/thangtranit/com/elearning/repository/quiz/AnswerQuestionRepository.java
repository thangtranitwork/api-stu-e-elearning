package thangtranit.com.elearning.repository.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.quiz.AnswerQuestion;

@Repository
public interface AnswerQuestionRepository extends JpaRepository<AnswerQuestion, String> {
}
