package thangtranit.com.elearning.repository.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.course.Progress;

import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, String> {
    Optional<Progress> findByEnrollmentIdAndLessonId(String enrollmentId, String lessonId);
    Optional<Progress> findByLessonIdAndEnrollmentStudentId(String lessonId, String studentId);
}
