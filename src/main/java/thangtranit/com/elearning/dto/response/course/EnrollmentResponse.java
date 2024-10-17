package thangtranit.com.elearning.dto.response.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentResponse {
    String id;
    CourseMinimumInfoResponse course;
    UserMinimumInfoResponse student;
    LocalDateTime startTime;
    LocalDateTime completeTime;
}
