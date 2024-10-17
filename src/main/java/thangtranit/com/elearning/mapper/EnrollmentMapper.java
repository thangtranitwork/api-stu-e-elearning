package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import thangtranit.com.elearning.dto.response.course.EnrollmentResponse;
import thangtranit.com.elearning.entity.course.Enrollment;

@Mapper(componentModel = "spring", uses = {CourseMapper.class, UserMapper.class})
public abstract class EnrollmentMapper {
    public abstract EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);
}
