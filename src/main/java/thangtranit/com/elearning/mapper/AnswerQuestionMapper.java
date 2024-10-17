package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import thangtranit.com.elearning.dto.request.quiz.AnswerQuestionRequest;
import thangtranit.com.elearning.entity.quiz.Answer;
import thangtranit.com.elearning.entity.quiz.AnswerQuestion;
import thangtranit.com.elearning.entity.quiz.Question;
import thangtranit.com.elearning.service.quiz.AnswerService;
import thangtranit.com.elearning.service.quiz.QuestionService;

@Mapper(componentModel = "spring")
public abstract class AnswerQuestionMapper {
    @Autowired
    protected AnswerService answerService;
    @Autowired
    protected QuestionService questionService;

    @Mapping(target = "answer", expression = "java(mapAnswer(request))")
    @Mapping(target = "question", expression = "java(mapQuestion(request))")
    public abstract AnswerQuestion toAnswerQuestion(AnswerQuestionRequest request);

    protected Answer mapAnswer(AnswerQuestionRequest request){
        return answerService.getAnswer(request.getAnswerId());
    }

    protected Question mapQuestion(AnswerQuestionRequest request){
        return questionService.getQuestion(request.getQuestionId());
    }
}
