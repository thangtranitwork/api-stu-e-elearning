package thangtranit.com.elearning.repository.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.course.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Page<Course> findAllByNameContainingOrderByCreatedAtDesc(String name, Pageable pageable);
    @Query("SELECT c FROM Course c ORDER BY SIZE(c.enrollments) DESC LIMIT 3")
    List<Course> findTop3ByOrderByEnrollmentsDesc();
    Page<Course> findAllByCreatorIdOrderByCreatedAtDesc(String creatorId, Pageable pageable);
}
