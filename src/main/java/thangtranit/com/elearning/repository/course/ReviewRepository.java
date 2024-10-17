package thangtranit.com.elearning.repository.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.course.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Page<Review> findByCourseIdOrderByCreatedTimeDesc(String courseId, Pageable pageable);

    Optional<Review> findByCourseIdAndReviewerId(String courseId, String reviewerId);

    boolean existsByCourseIdAndReviewerId(String courseId, String reviewerId);

}
