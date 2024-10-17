package thangtranit.com.elearning.repository.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.quiz.Quiz;

import java.util.List;


@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    Page<Quiz> findAllByNameContainingOrderByCreatedTimeDesc(String name, Pageable pageable);
    @Query("SELECT q FROM Quiz q ORDER BY SIZE(q.answerQuizList) DESC LIMIT 3")
    List<Quiz> findTop3ByOrderByPlayedTimeDesc();
    Page<Quiz> findAllByUserIdOrderByCreatedTimeDesc(String userId, Pageable pageable);
}
