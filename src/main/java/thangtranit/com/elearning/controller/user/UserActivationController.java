package thangtranit.com.elearning.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.course.CourseMinimumInfoResponse;
import thangtranit.com.elearning.dto.response.quiz.QuizInfoResponse;
import thangtranit.com.elearning.dto.response.quiz.UserAnswerQuizHistoryResponse;
import thangtranit.com.elearning.service.user.UserActivationService;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActivationController {
    final UserActivationService userActivationService;

    @GetMapping("/{id}/quizzes/played")
    ApiResponse<Page<UserAnswerQuizHistoryResponse>> getPlayedQuizzes(@PathVariable String id,
                                                                      @RequestParam(defaultValue = "0") int page) {

        return ApiResponse.<Page<UserAnswerQuizHistoryResponse>>builder()
                .body(userActivationService.getPlayedQuizzes(id, page))
                .build();
    }

    @GetMapping("/{id}/quizzes/created")
    ApiResponse<Page<QuizInfoResponse>> getCreatedQuizzes(@PathVariable String id,
                                                          @RequestParam(defaultValue = "0") int page) {

        return ApiResponse.<Page<QuizInfoResponse>>builder()
                .body(userActivationService.getCreatedQuizzes(id, page))
                .build();
    }

    @GetMapping("/{id}/courses/created")
    ApiResponse<Page<CourseMinimumInfoResponse>> getCreatedCourses(@PathVariable String id,
                                                                   @RequestParam(defaultValue = "0") int page) {

        return ApiResponse.<Page<CourseMinimumInfoResponse>>builder()
                .body(userActivationService.getCreatedCourses(id, page))
                .build();
    }

    @GetMapping("/{id}/courses/learned")
    ApiResponse<Page<CourseMinimumInfoResponse>> getLearnedCourses(@PathVariable String id,
                                                                   @RequestParam(defaultValue = "0") int page) {

        return ApiResponse.<Page<CourseMinimumInfoResponse>>builder()
                .body(userActivationService.getLearnedCourses(id, page))
                .build();
    }


}
