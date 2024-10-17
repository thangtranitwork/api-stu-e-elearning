package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import thangtranit.com.elearning.dto.response.quiz.QuestionResponse;
import thangtranit.com.elearning.dto.response.quiz.StartAnswerQuizResponse;
import thangtranit.com.elearning.dto.response.quiz.UserAnswerQuizHistoryResponse;
import thangtranit.com.elearning.entity.quiz.AnswerQuiz;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class, QuizMapper.class})
public abstract class AnswerQuizMapper {
    @Autowired
    private QuestionMapper questionMapper;

    @Mapping(target = "name", expression = "java(mapQuizName(answerQuiz))")
    @Mapping(target = "duration", expression = "java(mapDuration(answerQuiz))")
    @Mapping(target = "remainingTime", expression = "java(mapRemainingTime(answerQuiz))")
    @Mapping(target = "questions", expression = "java(mapQuestions(answerQuiz))")
    public abstract StartAnswerQuizResponse toStartAnswerQuizResponse(AnswerQuiz answerQuiz);

    @Mapping(target = "isComplete", expression = "java(mapIsComplete(answerQuiz))")
    @Mapping(target = "numberOfQuestions", expression = "java(mapNumberOfQuestions(answerQuiz))")
    @Mapping(target = "canContinue", expression = "java(mapCanContinue(answerQuiz))")
    public abstract UserAnswerQuizHistoryResponse toUserAnswerQuizHistoryResponse(AnswerQuiz answerQuiz);

    protected List<QuestionResponse> mapQuestions(AnswerQuiz answerQuiz) {
        List<QuestionResponse> questionResponses = new java.util.ArrayList<>(answerQuiz.getQuiz().getQuestions().stream().map(questionMapper::toQuestionResponse).toList());
         Collections.shuffle(questionResponses);
         return questionResponses;
    }
    protected String mapQuizName(AnswerQuiz answerQuiz) {
        return answerQuiz.getQuiz().getName();
    }

    protected int mapDuration(AnswerQuiz answerQuiz) {
        return answerQuiz.getQuiz().getDuration();
    }

    protected long mapRemainingTime(AnswerQuiz answerQuiz) {
        return answerQuiz.getRemainingTime();
    }

    protected int mapNumberOfQuestions(AnswerQuiz answerQuiz) {
        return Optional.of((answerQuiz.getQuiz().getQuestions().size())).orElse(0);
    }

    protected Boolean mapIsComplete(AnswerQuiz answerQuiz) {
        return answerQuiz.isComplete();
    }

    protected Boolean mapCanContinue(AnswerQuiz answerQuiz) {
        return !answerQuiz.isComplete() && answerQuiz.getTimeOut().isAfter(LocalDateTime.now());
    }
}
