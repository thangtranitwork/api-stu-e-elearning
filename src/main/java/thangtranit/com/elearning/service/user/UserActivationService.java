package thangtranit.com.elearning.service.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.response.course.CourseMinimumInfoResponse;
import thangtranit.com.elearning.dto.response.quiz.QuizInfoResponse;
import thangtranit.com.elearning.dto.response.quiz.UserAnswerQuizHistoryResponse;
import thangtranit.com.elearning.mapper.AnswerQuizMapper;
import thangtranit.com.elearning.mapper.CourseMapper;
import thangtranit.com.elearning.mapper.QuizMapper;
import thangtranit.com.elearning.repository.course.CourseRepository;
import thangtranit.com.elearning.repository.course.EnrollmentRepository;
import thangtranit.com.elearning.repository.quiz.AnswerQuizRepository;
import thangtranit.com.elearning.repository.quiz.QuizRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActivationService {
    final AnswerQuizMapper answerQuizMapper;
    final AnswerQuizRepository answerQuizRepository;
    final QuizRepository quizRepository;
    final CourseRepository courseRepository;
    final EnrollmentRepository enrollmentRepository;
    final QuizMapper quizMapper;
    final CourseMapper courseMapper;
    @NonFinal
    final int size = 3;

    public Page<UserAnswerQuizHistoryResponse> getPlayedQuizzes(String id, int page) {
        Pageable pageable = PageRequest.of(page, size);
        return answerQuizRepository.findAllByUserIdOrderByAnswerTimeDesc(id, pageable).map(answerQuizMapper::toUserAnswerQuizHistoryResponse);
    }

    public Page<QuizInfoResponse> getCreatedQuizzes(String id, int page) {
        Pageable pageable = PageRequest.of(page, size);
        return quizRepository.findAllByUserIdOrderByCreatedTimeDesc(id, pageable).map(quizMapper::toQuizInfoResponse);
    }

    public Page<CourseMinimumInfoResponse> getCreatedCourses(String id, int page) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findAllByCreatorIdOrderByCreatedAtDesc(id, pageable).map(courseMapper::toCourseMinimumInfoResponse);
    }

    public Page<CourseMinimumInfoResponse> getLearnedCourses(String id, int page) {
        Pageable pageable = PageRequest.of(page, size);
        return enrollmentRepository.findAllByStudentIdOrderByStartTimeDesc(id, pageable).map(e -> courseMapper.toCourseMinimumInfoResponse(e.getCourse()));
    }
}
