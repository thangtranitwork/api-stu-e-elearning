package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import thangtranit.com.elearning.dto.request.course.CourseRequest;
import thangtranit.com.elearning.dto.response.course.CourseInfoResponse;
import thangtranit.com.elearning.dto.response.course.CourseMinimumInfoResponse;
import thangtranit.com.elearning.entity.course.Course;
import thangtranit.com.elearning.entity.course.Review;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LessonMapper.class})
public abstract class CourseMapper {

    @Mapping(target = "lessonCount", expression = "java(mapLessonCount(course))")
    @Mapping(target = "enrollmentCount", expression = "java(mapEnrollmentCount(course))")
    @Mapping(target = "star", expression = "java(mapStar(course))")
    public abstract CourseInfoResponse toCourseInfoResponse(Course course);
    @Mapping(target = "star", expression = "java(mapStar(course))")
    public abstract CourseMinimumInfoResponse toCourseMinimumInfoResponse(Course course);
    public abstract Course toCourse(CourseRequest request);
    public abstract void updateCourse(@MappingTarget Course course, CourseRequest request);

    protected int mapLessonCount(Course course){
        return Optional.ofNullable(course.getLessons()).map(List::size).orElse(0);
    }

    protected int mapEnrollmentCount(Course course){
        return Optional.ofNullable(course.getEnrollments()).map(List::size).orElse(0);
    }

    protected double mapStar(Course course) {
        return Optional.ofNullable(course.getReviews())
                .stream()
                .flatMap(List::stream)
                .mapToInt(Review::getStar)
                .average()
                .orElse(5.0);
    }

}
