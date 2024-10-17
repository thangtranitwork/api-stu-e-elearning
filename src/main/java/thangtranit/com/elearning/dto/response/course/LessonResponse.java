package thangtranit.com.elearning.dto.response.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonResponse {
    String id;
    String name;
    String theory;
    boolean isComplete;
    CourseMinimumInfoResponse course;

}
