package thangtranit.com.elearning.controller.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.course.LessonResponse;
import thangtranit.com.elearning.service.course.ProgressService;

@RestController
@RequestMapping("/api/courses/{courseId}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressController {

    final ProgressService progressService;

    @GetMapping("/lessons/{lessonId}")
    ApiResponse<LessonResponse> getLesson(@PathVariable String courseId, @PathVariable String lessonId){
        return ApiResponse.<LessonResponse>builder()
                .body(progressService.getLesson(courseId, lessonId))
                .build();
    }

    @GetMapping("/lessons/continue")
    ApiResponse<String> getLesson(@PathVariable String courseId){
        return ApiResponse.<String>builder()
                .body(progressService.getLatestLesson(courseId))
                .build();
    }
    @PatchMapping("/lessons/{lessonId}")
    ApiResponse<Void> complete(@PathVariable String courseId, @PathVariable String lessonId){
        progressService.completeLesson(courseId, lessonId);
        return ApiResponse.<Void>builder()
                .build();
    }

}
