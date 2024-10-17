package thangtranit.com.elearning.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseMinimumInfoResponse {
    String id;
    String name;
    String description;
    int price;
    double star;
    UserMinimumInfoResponse creator;
}
