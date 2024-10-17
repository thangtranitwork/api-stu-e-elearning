package thangtranit.com.elearning.service.quiz;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.quiz.AnswerRequest;
import thangtranit.com.elearning.dto.request.quiz.QuizRequest;
import thangtranit.com.elearning.dto.response.quiz.QuizInfoResponse;
import thangtranit.com.elearning.dto.response.quiz.QuizResponse;
import thangtranit.com.elearning.entity.quiz.Quiz;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.QuizMapper;
import thangtranit.com.elearning.repository.quiz.QuizRepository;
import thangtranit.com.elearning.service.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizService {
    QuizRepository quizRepository;
    QuizMapper quizMapper;
    UserService userService;
    public QuizResponse getQuizResponse(String id) {
        return quizMapper.toQuizResponse(getQuiz(id));
    }

    public Quiz getQuiz(String id) {
        return quizRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.QUIZ_DOES_NOT_EXIST));
    }

    public String getCreatorId(String id){
        return getQuiz(id).getUser().getId();
    }

    public QuizResponse createQuiz(QuizRequest request) {
        request.getQuestions().forEach(question -> {
            int correctAnswerCount = (int) question.getAnswers().stream()
                    .filter(AnswerRequest::getIsCorrect)
                    .count();
            if (correctAnswerCount != 1) {
                throw new AppException(ErrorCode.QUESTION_MUST_HAVE_ONE_CORRECT_ANSWER, Map.of("invalidQuestion", question.getText()));
            }
        });

        Quiz quiz = quizMapper.toQuiz(request);
        User creator = userService.getCurrentUser();
        quiz.setUser(creator);
        quiz.setQuestions(quiz.getQuestions());
        quizRepository.save(quiz);
        return quizMapper.toQuizResponse(quiz);
    }

    @PreAuthorize("@quizService.getCreatorId(#id) == @jwtUtil.getCurrentUserId()")
    public QuizResponse updateQuiz(String id, QuizRequest request) {
        Quiz quiz = getQuiz(id);
        quizMapper.updateQuiz(quiz, request);
        quizRepository.save(quiz);
        return quizMapper.toQuizResponse(quiz);
    }

    @PreAuthorize("@quizService.getCreatorId(#id) == @jwtUtil.getCurrentUserId()")
    public void deleteQuiz(String id) {
        Quiz quiz = getQuiz(id);
        quizRepository.delete(quiz);
    }

    public Page<QuizInfoResponse> findByName(String name, int page) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        return quizRepository.findAllByNameContainingOrderByCreatedTimeDesc(name, pageable)
                .map(quizMapper::toQuizInfoResponse);
    }

    public List<QuizInfoResponse> getHottestQuizzes() {
        return quizRepository.findTop3ByOrderByPlayedTimeDesc().stream().map(quizMapper::toQuizInfoResponse).collect(Collectors.toList());
    }

}
