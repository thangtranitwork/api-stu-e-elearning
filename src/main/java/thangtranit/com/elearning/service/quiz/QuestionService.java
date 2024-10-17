package thangtranit.com.elearning.service.quiz;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.entity.quiz.Question;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.repository.quiz.QuestionRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionService {
    QuestionRepository questionRepository;

    public Question getQuestion(String id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.QUESTION_DOES_NOT_EXISTS));
    }
}
