package thangtranit.com.elearning.repository.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.quiz.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
}
