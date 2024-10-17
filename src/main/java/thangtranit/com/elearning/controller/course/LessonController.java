package thangtranit.com.elearning.controller.course;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.course.LessonRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.course.LessonResponse;
import thangtranit.com.elearning.service.course.LessonService;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonController {
    final LessonService lessonService;
    @PostMapping("/new")
    ApiResponse<LessonResponse> addLesson(@PathVariable String courseId, @RequestBody @Valid LessonRequest request){
        return ApiResponse.<LessonResponse>builder()
                .body(lessonService.addLesson(courseId, request))
                .build();
    }

    @PutMapping("/{lessonId}")
    ApiResponse<Void> updateLesson(@PathVariable String lessonId, @PathVariable String courseId, @RequestBody @Valid LessonRequest request){
        lessonService.updateLesson(courseId, lessonId, request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/{lessonId}")
    ApiResponse<Void> deleteLesson(@PathVariable String courseId, @PathVariable String lessonId){
        lessonService.deleteLesson(courseId, lessonId);
        return ApiResponse.<Void>builder()
                .build();
    }
}
