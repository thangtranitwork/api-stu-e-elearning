package thangtranit.com.elearning.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonMinimumInfoResponse {
    String id;
    String name;
    boolean isCompleted;
    int sequenceNumber;
}
