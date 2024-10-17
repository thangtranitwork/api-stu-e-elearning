package thangtranit.com.elearning.service.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.response.course.LessonMinimumInfoResponse;
import thangtranit.com.elearning.dto.response.course.LessonResponse;
import thangtranit.com.elearning.entity.course.Enrollment;
import thangtranit.com.elearning.entity.course.Lesson;
import thangtranit.com.elearning.entity.course.Progress;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.LessonMapper;
import thangtranit.com.elearning.repository.course.ProgressRepository;
import thangtranit.com.elearning.service.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final LessonMapper lessonMapper;
    private final LessonService lessonService;
    private final ProgressRepository progressRepository;
    private final EnrollmentService enrollmentService;
    private final UserService userService;

    public LessonResponse getLesson(String courseId, String lessonId) {
        Lesson lesson = lessonService.getLesson(lessonId);

        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new AppException(ErrorCode.THE_LESSON_DOES_NOT_BELONG_TO_THE_COURSE);
        }

        Enrollment enrollment = enrollmentService.getEnrollment(courseId, userService.getCurrentUserId());

        Progress progress = progressRepository.findByEnrollmentIdAndLessonId(enrollment.getId(), lessonId)
                .orElseGet(() -> {
                    Progress newProgress = Progress.builder()
                            .enrollment(enrollment)
                            .lesson(lesson)
                            .build();
                    progressRepository.save(newProgress);
                    return newProgress;
                });

        LessonResponse response = lessonMapper.toLessonResponse(lesson);
        response.setComplete(progress.isCompleted());
        return response;
    }

    public String getLatestLesson(String courseId) {
        String currentUserId = userService.getCurrentUserId();
        Enrollment enrollment = enrollmentService.getEnrollment(courseId, currentUserId);
        List<Lesson> lessons = enrollment.getCourse().getLessons();
        Lesson latestLesson = lessons.stream().filter(lesson ->
                        progressRepository.findByLessonIdAndEnrollmentStudentId(lesson.getId(), currentUserId)
                                .isEmpty())
                .findFirst()
                .orElse(lessons.get(lessons.size() - 1));
        Progress progress = progressRepository.findByEnrollmentIdAndLessonId(enrollment.getId(), latestLesson.getId())
                .orElseGet(() -> {
                    Progress newProgress = Progress.builder()
                            .enrollment(enrollment)
                            .lesson(latestLesson)
                            .build();
                    progressRepository.save(newProgress);
                    return newProgress;
                });
        progressRepository.save(progress);

        return latestLesson.getId();
    }

    public void completeLesson(String courseId, String lessonId) {
        Lesson lesson = lessonService.getLesson(lessonId);

        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new AppException(ErrorCode.THE_LESSON_DOES_NOT_BELONG_TO_THE_COURSE);
        }

        Enrollment enrollment = enrollmentService.getEnrollment(courseId, userService.getCurrentUserId());

        Progress progress = progressRepository.findByEnrollmentIdAndLessonId(enrollment.getId(), lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.THE_USER_HAS_NOT_STARTED_THIS_LESSON));

        progress.setCompleted(true);
        progressRepository.save(progress);
    }

    public List<LessonMinimumInfoResponse> getSyllabus(String courseId) {
        Map<String, Boolean> lessonProgressMap; // Khởi tạo Map rỗng mặc định


        String currentUserId = userService.getCurrentUserId();
        Enrollment enrollment = enrollmentService.getEnrollment(courseId, currentUserId);

        lessonProgressMap = getLessonProgressMap(enrollment.getProgresses());


        return enrollment.getCourse().getLessons().stream()
                .map(lesson -> mapToLessonMinimumInfoResponse(lesson, lessonProgressMap))
                .collect(Collectors.toList());
    }

    private Map<String, Boolean> getLessonProgressMap(List<Progress> progresses) {
        return progresses.stream()
                .collect(Collectors.toMap(
                        progress -> progress.getLesson().getId(),
                        Progress::isCompleted
                ));
    }

    private LessonMinimumInfoResponse mapToLessonMinimumInfoResponse(Lesson lesson, Map<String, Boolean> lessonProgressMap) {
        LessonMinimumInfoResponse lessonInfoResponse = lessonMapper.toLessonMinimumInfoResponse(lesson);
        boolean isCompleted = lessonProgressMap.getOrDefault(lesson.getId(), false);
        lessonInfoResponse.setCompleted(isCompleted);
        return lessonInfoResponse;
    }
}
