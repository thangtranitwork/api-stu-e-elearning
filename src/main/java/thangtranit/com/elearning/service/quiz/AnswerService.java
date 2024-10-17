package thangtranit.com.elearning.service.quiz;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.entity.quiz.Answer;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.repository.quiz.AnswerRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnswerService {
    AnswerRepository answerRepository;

    public Answer getAnswer(String id) {
        return answerRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ANSWER_DOES_NOT_EXIST));
    }
}
