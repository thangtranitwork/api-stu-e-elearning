package thangtranit.com.elearning.service.quiz;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.quiz.AnswerQuestionRequest;
import thangtranit.com.elearning.dto.request.quiz.SubmitQuizRequest;
import thangtranit.com.elearning.dto.response.quiz.QuestionResultResponse;
import thangtranit.com.elearning.dto.response.quiz.StartAnswerQuizResponse;
import thangtranit.com.elearning.dto.response.quiz.SubmitQuizResponse;
import thangtranit.com.elearning.entity.quiz.AnswerQuestion;
import thangtranit.com.elearning.entity.quiz.AnswerQuiz;
import thangtranit.com.elearning.entity.quiz.Question;
import thangtranit.com.elearning.entity.quiz.Quiz;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.AnswerQuestionMapper;
import thangtranit.com.elearning.mapper.AnswerQuizMapper;
import thangtranit.com.elearning.repository.quiz.AnswerQuizRepository;
import thangtranit.com.elearning.service.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnswerQuizService {
    AnswerQuizRepository answerQuizRepository;
    AnswerQuizMapper answerQuizMapper;
    AnswerQuestionMapper answerQuestionMapper;
    QuizService quizService;
    UserService userService;

    public StartAnswerQuizResponse start(String quizId) {
        String userId = userService.getCurrentUserId();

        AnswerQuiz answerQuiz = continueOrCreateAnswerQuiz(quizId, userId);

        return answerQuizMapper.toStartAnswerQuizResponse(answerQuiz);
    }

    private AnswerQuiz continueOrCreateAnswerQuiz(String quizId, String userId) {
        Optional<AnswerQuiz> optionalAnswerQuiz = answerQuizRepository.findByQuizIdAndUserIdCanContinue(quizId, userId);

        if (optionalAnswerQuiz.isPresent() && optionalAnswerQuiz.get().getRemainingTime() > 0) {
            return optionalAnswerQuiz.get();
        }

        return createAndSaveAnswerQuiz(quizId, userId);
    }

    private AnswerQuiz createAndSaveAnswerQuiz(String quizId, String userId) {
        Quiz quiz = quizService.getQuiz(quizId);
        User user = userService.getUser(userId);
        AnswerQuiz answerQuiz = AnswerQuiz.builder()
                .quiz(quiz)
                .user(user)
                .timeOut(LocalDateTime.now().plusMinutes(quiz.getDuration()))
                .build();

        return answerQuizRepository.save(answerQuiz);
    }

    public SubmitQuizResponse submit(String id, SubmitQuizRequest request) {
        String userId = userService.getCurrentUserId();
        AnswerQuiz answerQuiz = answerQuizRepository.findByQuizIdAndUserIdCanContinue(id, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_QUIZ_NOT_FOUND_OR_TIMEOUT));

        validateNumberOfAnswers(request, answerQuiz);
        validateQuestionsInQuiz(request, answerQuiz);

        List<AnswerQuestion> answerQuestions = mapToAnswerQuestions(request);
        List<QuestionResultResponse> questionResultResponses = new ArrayList<>();
        int score = calculateScoreAndGenerateResponses(answerQuestions, questionResultResponses);
        updateAnswer(answerQuiz, answerQuestions, score);

        return SubmitQuizResponse.builder()
                .questionResultResponses(questionResultResponses)
                .score(score)
                .build();
    }

    private void updateAnswer(AnswerQuiz answerQuiz, List<AnswerQuestion> answerQuestions, int score) {
        answerQuiz.setAnswerQuestions(answerQuestions);
        answerQuiz.setScore(score);
        answerQuiz.setComplete(true);
        answerQuizRepository.save(answerQuiz);
    }

    private void validateNumberOfAnswers(SubmitQuizRequest request, AnswerQuiz answerQuiz) {
        if (request.getAnswers().size() != answerQuiz.getQuiz().getQuestions().size()) {
            throw new AppException(ErrorCode.NUMBER_OF_ANSWERS_DOES_NOT_MATCH);
        }
    }

    private void validateQuestionsInQuiz(SubmitQuizRequest request, AnswerQuiz answerQuiz) {
        List<String> quizQuestionIds = answerQuiz.getQuiz().getQuestions().stream()
                .map(Question::getId)
                .toList();
        List<String> requestQuestionIds = request.getAnswers().stream()
                .map(AnswerQuestionRequest::getQuestionId)
                .toList();

        if (!new HashSet<>(requestQuestionIds).containsAll(quizQuestionIds)) {
            throw new AppException(ErrorCode.ANSWER_QUIZ_DOES_NOT_CONTAIN_ALL_QUESTIONS);
        }
    }

    private List<AnswerQuestion> mapToAnswerQuestions(SubmitQuizRequest request) {
        return request.getAnswers().stream()
                .map(answerQuestionMapper::toAnswerQuestion)
                .collect(Collectors.toList());
    }

    private int calculateScoreAndGenerateResponses(List<AnswerQuestion> answerQuestions, List<QuestionResultResponse> questionResultResponses) {
        return answerQuestions.stream().mapToInt(answerQuestion -> {
            validateAnswerBeIntegersToQuestion(answerQuestion);

            String correctId = answerQuestion.getQuestion().getCorrectAnswer().getId();
            String chosenId = answerQuestion.getAnswer().getId();

            questionResultResponses.add(QuestionResultResponse.builder()
                    .id(answerQuestion.getQuestion().getId())
                    .chosenId(chosenId)
                    .correctId(correctId)
                    .build());

            return (chosenId.equals(correctId)) ? 1 : 0;
        }).sum();
    }

    private void validateAnswerBeIntegersToQuestion(AnswerQuestion answerQuestion) {
        if (answerQuestion.getAnswer().getQuestion() != answerQuestion.getQuestion()) {
            throw new AppException(ErrorCode.THE_ANSWER_DOES_NOT_BELONG_TO_THE_QUESTION);
        }
    }
}
