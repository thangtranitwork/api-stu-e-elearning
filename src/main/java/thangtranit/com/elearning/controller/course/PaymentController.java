package thangtranit.com.elearning.controller.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.service.course.EnrollmentService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentController {
    final EnrollmentService enrollmentService;
    @Value("${FE_ORIGIN}")
    String FE_ORIGIN;
    @PostMapping("/buy")
    ApiResponse<String> buyCourse(@RequestParam(name = "id") String courseId,
                                  HttpServletRequest request) throws JsonProcessingException {
        return ApiResponse.<String>builder()
                .body(enrollmentService.buy(courseId, request))
                .build();
    }

    @GetMapping("/payment-return")
    void buyCourseCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = enrollmentService.processPayment(request);
        response.sendRedirect(FE_ORIGIN.concat("/courses/" + id));
    }
}
