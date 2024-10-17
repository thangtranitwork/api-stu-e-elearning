package thangtranit.com.elearning.controller.quiz;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.quiz.QuizRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.quiz.QuizInfoResponse;
import thangtranit.com.elearning.dto.response.quiz.QuizResponse;
import thangtranit.com.elearning.service.quiz.QuizService;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizController {
    final QuizService quizService;
    final SimpMessagingTemplate messagingTemplate;
    @GetMapping("/search")
    ApiResponse<Page<QuizInfoResponse>> findQuiz(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page) {

        return ApiResponse.<Page<QuizInfoResponse>>builder()
                .body(quizService.findByName(name, page))
                .build();
    }

    @GetMapping("/hottest")
    ApiResponse<List<QuizInfoResponse>> getHottestQuizzes() {
        return ApiResponse.<List<QuizInfoResponse>>builder()
                .body(quizService.getHottestQuizzes())
                .build();
    }

    @PostMapping("/new")
    ApiResponse<QuizResponse> addQuiz(@RequestBody @Valid QuizRequest request){
        QuizResponse response = quizService.createQuiz(request);
        messagingTemplate.convertAndSend("/quiz" , response);
        return ApiResponse.<QuizResponse>builder()
                .body(response)
                .build();
    }

    @PutMapping("/{id}/update")
    ApiResponse<QuizResponse> updateQuiz(@PathVariable String id, @RequestBody @Valid QuizRequest request){
        return ApiResponse.<QuizResponse>builder()
                .body(quizService.updateQuiz(id, request))
                .build();
    }


    @DeleteMapping("/{id}/delete")
    ApiResponse<Void> deleteQuiz(@PathVariable String id){
        quizService.deleteQuiz(id);
        return ApiResponse.<Void>builder()
                .build();
    }
}
