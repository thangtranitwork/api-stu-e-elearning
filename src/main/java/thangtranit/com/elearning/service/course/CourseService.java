package thangtranit.com.elearning.service.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.course.CourseRequest;
import thangtranit.com.elearning.dto.response.course.CourseInfoResponse;
import thangtranit.com.elearning.dto.response.course.CourseMinimumInfoResponse;
import thangtranit.com.elearning.dto.response.course.LessonMinimumInfoResponse;
import thangtranit.com.elearning.entity.course.Course;
import thangtranit.com.elearning.entity.course.Enrollment;
import thangtranit.com.elearning.entity.course.Progress;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.CourseMapper;
import thangtranit.com.elearning.repository.course.CourseRepository;
import thangtranit.com.elearning.repository.course.EnrollmentRepository;
import thangtranit.com.elearning.repository.course.ReviewRepository;
import thangtranit.com.elearning.service.user.UserService;
import thangtranit.com.elearning.service.util.JwtUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public Course getCourse(String id) {
        return courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTS));
    }

    public CourseInfoResponse getCourseInfoResponse(String id) {
        Course course = getCourse(id);
        CourseInfoResponse courseInfoResponse = courseMapper.toCourseInfoResponse(course);
        courseInfoResponse.getLessons()
                .sort(Comparator.comparing(LessonMinimumInfoResponse::getSequenceNumber));
        if (!userService.isAnonymous()) {
            String currentUserId = jwtUtil.getCurrentUserId();
            Optional<Enrollment> enrollment = enrollmentRepository.findByCourseIdAndStudentId(id, currentUserId);
            enrollment.ifPresent((en) -> {
                courseInfoResponse.setEnroll(true);
                Map<String, Boolean> lessonProgressMap = getLessonProgressMap(en.getProgresses());
                updateLessonCompletionStatus(courseInfoResponse, lessonProgressMap);
                boolean canReview = !course.getLessons().isEmpty() && !reviewRepository.existsByCourseIdAndReviewerId(course.getId(), currentUserId) && (course.getLessons().size() / 2 <= en.getProgresses().size());

                courseInfoResponse.setCanReview(canReview);
            });
        }

        return courseInfoResponse;
    }


    private Map<String, Boolean> getLessonProgressMap(List<Progress> progresses) {
        return progresses.stream()
                .collect(Collectors.toMap(lp -> lp.getLesson().getId(), Progress::isCompleted));
    }

    private void updateLessonCompletionStatus(CourseInfoResponse courseInfoResponse, Map<String, Boolean> lessonProgressMap) {
        courseInfoResponse.getLessons().forEach(lessonInfoResponse -> {
            boolean isCompleted = lessonProgressMap.getOrDefault(lessonInfoResponse.getId(), false);
            lessonInfoResponse.setCompleted(isCompleted);
        });
    }

    public String getCreatorId(String id) {
        return getCourse(id).getCreator().getId();
    }

    public Page<CourseMinimumInfoResponse> findByName(String name, int page) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Course> courses = courseRepository.findAllByNameContainingOrderByCreatedAtDesc(name, pageable);
        return courses.map(courseMapper::toCourseMinimumInfoResponse);
    }

    public CourseMinimumInfoResponse create(CourseRequest request) {
        Course course = courseMapper.toCourse(request);
        User creator = userService.getUser(jwtUtil.getCurrentUserId());
        course.setCreator(creator);
        courseRepository.save(course);
        return courseMapper.toCourseMinimumInfoResponse(courseRepository.save(course));
    }

    @PreAuthorize("@courseService.getCreatorId(#id) == @userService.getCurrentUserId()")
    public void update(String id, CourseRequest request) {
        Course course = getCourse(id);
        courseMapper.updateCourse(course, request);
        courseRepository.save(course);
    }

    @PreAuthorize("@courseService.getCreatorId(#id) == @userService.getCurrentUserId()")
    public void delete(String id) {
        courseRepository.delete(getCourse(id));
    }

    public List<CourseMinimumInfoResponse> getHottestCourses() {
        return courseRepository.findTop3ByOrderByEnrollmentsDesc()
                .stream()
                .map(courseMapper::toCourseMinimumInfoResponse)
                .collect(Collectors.toList());
    }
}
