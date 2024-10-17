package thangtranit.com.elearning.repository.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.course.Enrollment;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    Optional<Enrollment> findByCourseIdAndStudentId(String courseId, String studentId);
    Page<Enrollment> findAllByStudentIdOrderByStartTimeDesc(String studentId, Pageable pageable);
}
