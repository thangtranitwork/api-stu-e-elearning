package thangtranit.com.elearning.controller.course;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.course.CourseRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.course.CourseInfoResponse;
import thangtranit.com.elearning.dto.response.course.CourseMinimumInfoResponse;
import thangtranit.com.elearning.service.course.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseController {
    final CourseService courseService;

    @GetMapping("/{id}")
    ApiResponse<CourseInfoResponse> getCourse(@PathVariable String id) {
        return ApiResponse.<CourseInfoResponse>builder()
                .body(courseService.getCourseInfoResponse(id))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<Page<CourseMinimumInfoResponse>> findCourse(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page) {

        return ApiResponse.<Page<CourseMinimumInfoResponse>>builder()
                .body(courseService.findByName(name, page))
                .build();
    }

    @GetMapping("/hottest")
    ApiResponse<List<CourseMinimumInfoResponse>> getHottestCourse(){
        return ApiResponse.<List<CourseMinimumInfoResponse>>builder()
                .body(courseService.getHottestCourses())
                .build();
    }

    @PostMapping("/new")
    ApiResponse<CourseMinimumInfoResponse> createCourse(@RequestBody @Valid CourseRequest request) {
        return ApiResponse.<CourseMinimumInfoResponse>builder()
                .body(courseService.create(request))
                .build();
    }

    @PutMapping("/{id}/update")
    ApiResponse<Void> updateCourse(@PathVariable String id, @RequestBody @Valid CourseRequest request) {
        courseService.update(id, request);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{id}/delete")
    ApiResponse<Void> deleteCourse(@PathVariable String id) {
        courseService.delete(id);
        return ApiResponse.<Void>builder().build();
    }
}
