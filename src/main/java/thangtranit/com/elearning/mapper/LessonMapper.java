package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import thangtranit.com.elearning.dto.request.course.LessonRequest;
import thangtranit.com.elearning.dto.response.course.LessonMinimumInfoResponse;
import thangtranit.com.elearning.dto.response.course.LessonResponse;
import thangtranit.com.elearning.entity.course.Lesson;

@Mapper(componentModel = "spring")
public abstract class LessonMapper {
    public abstract LessonResponse toLessonResponse(Lesson lesson);
    public abstract LessonMinimumInfoResponse toLessonMinimumInfoResponse(Lesson lesson);
    public abstract Lesson toLesson(LessonRequest request);

    public abstract void updateLesson(@MappingTarget Lesson lesson, LessonRequest request);
}
