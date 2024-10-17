package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thangtranit.com.elearning.dto.request.quiz.AnswerRequest;
import thangtranit.com.elearning.dto.response.quiz.AnswerResponse;
import thangtranit.com.elearning.entity.quiz.Answer;

@Mapper(componentModel = "spring")
public abstract class AnswerMapper {
    @Mapping(target = "isCorrect", source = "request.isCorrect")
    public abstract Answer toAnswer(AnswerRequest request);
    public abstract AnswerResponse toAnswerResponse(Answer answer);
}
