package thangtranit.com.elearning.repository.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.course.Lesson;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    int countByCourseId(String courseId);
    Optional<Lesson> findBySequenceNumber(int sequenceNumber);
}
