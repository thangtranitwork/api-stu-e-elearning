package thangtranit.com.elearning.service.course;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.course.LessonRequest;
import thangtranit.com.elearning.dto.response.course.LessonResponse;
import thangtranit.com.elearning.entity.course.Course;
import thangtranit.com.elearning.entity.course.Lesson;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.LessonMapper;
import thangtranit.com.elearning.repository.course.CourseRepository;
import thangtranit.com.elearning.repository.course.LessonRepository;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;
    private final CourseService courseService;
    private final LessonRepository lessonRepository;

    public Lesson getLesson(String id) {
        return lessonRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_EXISTS));
    }

    @PreAuthorize("@courseService.getCreatorId(#quizId) == @jwtUtil.getCurrentUserId()")
    public LessonResponse addLesson(String quizId, LessonRequest request) {
        Course course = courseService.getCourse(quizId);
        Lesson lesson = lessonMapper.toLesson(request);
        lesson.setSequenceNumber(course.getLessons().size() + 1);
        course.addLesson(lesson);
        courseRepository.save(course);
        return lessonMapper.toLessonResponse(lesson);
    }

    @PreAuthorize("@courseService.getCreatorId(#courseId) == @jwtUtil.getCurrentUserId()")
    public void updateLesson(String courseId, String lessonId, LessonRequest request) {
        Lesson lesson = getLesson(lessonId);
        if(!lesson.getCourse().getId().equals(courseId)) {
            throw new AppException(ErrorCode.THE_LESSON_DOES_NOT_BELONG_TO_THE_COURSE);
        }
        lessonMapper.updateLesson(lesson, request);
        lessonRepository.save(lesson);
    }

    @PreAuthorize("@courseService.getCreatorId(#courseId) == @jwtUtil.getCurrentUserId()")
    public void deleteLesson(String courseId, String lessonId) {
        Lesson lesson = getLesson(lessonId);
        if(!lesson.getCourse().getId().equals(courseId)){
            throw new AppException(ErrorCode.THE_LESSON_DOES_NOT_BELONG_TO_THE_COURSE);
        }
        lessonRepository.delete(lesson);
    }
}
