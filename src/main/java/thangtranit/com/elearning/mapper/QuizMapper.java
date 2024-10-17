package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import thangtranit.com.elearning.dto.request.quiz.QuizRequest;
import thangtranit.com.elearning.dto.response.quiz.QuizInfoResponse;
import thangtranit.com.elearning.dto.response.quiz.QuizResponse;
import thangtranit.com.elearning.entity.quiz.Quiz;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class, UserMapper.class})
public abstract class QuizMapper {
    public abstract Quiz toQuiz(QuizRequest request);

    @Mapping(target = "playedTimes", expression = "java(mapPlayedTimes(quiz))")
    @Mapping(target = "numberOfQuestions", expression = "java(mapNumberOfQuestions(quiz))")
    public abstract QuizInfoResponse toQuizInfoResponse(Quiz quiz);

    public abstract QuizResponse toQuizResponse(Quiz quiz);

    public abstract void updateQuiz(@MappingTarget Quiz quiz, QuizRequest request);


    protected int mapNumberOfQuestions(Quiz quiz) {
        return Optional.of((quiz.getQuestions().size())).orElse(0);
    }

    protected int mapPlayedTimes(Quiz quiz) {
        return Optional.ofNullable(quiz.getAnswerQuizList()).map(List::size).orElse(0);
    }


}
