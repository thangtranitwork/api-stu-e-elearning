package thangtranit.com.elearning.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreatedCourseResponse {
    String id;
    String name;
    String description;
    int price;
    int lessonCount;
    int enrollmentCount;
}
