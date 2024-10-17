package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import thangtranit.com.elearning.dto.request.quiz.QuestionRequest;
import thangtranit.com.elearning.dto.response.quiz.QuestionResponse;
import thangtranit.com.elearning.entity.quiz.Question;

@Mapper(componentModel = "spring", uses = {AnswerMapper.class})
public abstract class QuestionMapper {

    public abstract Question toQuestion(QuestionRequest request);
    public abstract QuestionResponse toQuestionResponse(Question question);

}
