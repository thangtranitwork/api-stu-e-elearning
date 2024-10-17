package thangtranit.com.elearning.service.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.course.ReviewRequest;
import thangtranit.com.elearning.dto.response.course.ReviewResponse;
import thangtranit.com.elearning.entity.course.Course;
import thangtranit.com.elearning.entity.course.Review;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.ReviewMapper;
import thangtranit.com.elearning.repository.course.ReviewRepository;
import thangtranit.com.elearning.service.user.UserService;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final CourseService courseService;
    private final UserService userService;

    public void review(String courseId,ReviewRequest request) {
        Course course = courseService.getCourse(courseId);
        User reviewer = userService.getCurrentUser();
        if(reviewRepository.existsByCourseIdAndReviewerId(courseId, reviewer.getId())){
            throw new AppException(ErrorCode.THIS_USER_HAS_ALREADY_REVIEWED_THIS_COURSE);
        }
        Review review = reviewMapper.toReview(request);
        review.setCourse(course);
        review.setReviewer(reviewer);
        reviewRepository.save(review);
    }

    public Page<ReviewResponse> getReviewsByCourseId(String courseId, int page) {
        courseService.getCourse(courseId);
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        return reviewRepository.findByCourseIdOrderByCreatedTimeDesc(courseId, pageable).map(reviewMapper::toReviewResponse);
    }
}
