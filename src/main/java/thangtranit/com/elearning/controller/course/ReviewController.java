package thangtranit.com.elearning.controller.course;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.course.ReviewRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.course.ReviewResponse;
import thangtranit.com.elearning.service.course.ReviewService;

@RestController
@RequestMapping("/api/courses/{courseId}/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewController {
    final ReviewService reviewService;

    @PostMapping("/new")
    ApiResponse<Void> addReview(@PathVariable String courseId, @RequestBody @Valid ReviewRequest request) {
        reviewService.review(courseId, request);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("")
    ApiResponse<Page<ReviewResponse>> getReviews(
            @PathVariable String courseId,
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<ReviewResponse>>builder()
                .body(reviewService.getReviewsByCourseId(courseId, page))
                .build();
    }
}
