package thangtranit.com.elearning.controller.quiz;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.quiz.SubmitQuizRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.quiz.SubmitQuizResponse;
import thangtranit.com.elearning.dto.response.quiz.StartAnswerQuizResponse;
import thangtranit.com.elearning.service.quiz.AnswerQuizService;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerQuizController {
    final AnswerQuizService answerQuizService;
    @GetMapping("/{id}")
    ApiResponse<StartAnswerQuizResponse> start(@PathVariable String id){
        return ApiResponse.<StartAnswerQuizResponse>builder()
                .body(answerQuizService.start(id))
                .build();
    }

    @PostMapping("/{id}")
    ApiResponse<SubmitQuizResponse> submit(@PathVariable String id, @RequestBody SubmitQuizRequest request){
        return ApiResponse.<SubmitQuizResponse>builder()
                .body(answerQuizService.submit(id, request))
                .build();
    }
}
