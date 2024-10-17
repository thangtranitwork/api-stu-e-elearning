package thangtranit.com.elearning.dto.response.post;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String content;
    LocalDateTime createdAt;
    UserMinimumInfoResponse creator;

}
