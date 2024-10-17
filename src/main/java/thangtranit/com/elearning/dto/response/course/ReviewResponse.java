package thangtranit.com.elearning.dto.response.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    byte star;
    String text;
    UserMinimumInfoResponse reviewer;
    LocalDateTime createdTime;
}
