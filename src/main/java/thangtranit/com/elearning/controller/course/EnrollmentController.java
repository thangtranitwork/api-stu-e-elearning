package thangtranit.com.elearning.controller.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.course.LessonMinimumInfoResponse;
import thangtranit.com.elearning.service.course.EnrollmentService;
import thangtranit.com.elearning.service.course.ProgressService;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentController {
    final EnrollmentService enrollmentService;
    final ProgressService progressService;

    @PostMapping("/enroll")
    ApiResponse<Void> enroll(@PathVariable String courseId) {
        enrollmentService.enroll(courseId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/syllabus")
    ApiResponse<List<LessonMinimumInfoResponse>> getSyllabus(@PathVariable String courseId) {
        return ApiResponse.<List<LessonMinimumInfoResponse>>builder()
                .body(progressService.getSyllabus(courseId))
                .build();
    }
}
