package thangtranit.com.elearning.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseInfoResponse {
    String id;
    String name;
    String description;
    String introduction;
    int price;
    double star;
    int lessonCount;
    int enrollmentCount;
    boolean isEnroll;
    boolean canReview;
    UserMinimumInfoResponse creator;
    List<LessonMinimumInfoResponse> lessons;
}
