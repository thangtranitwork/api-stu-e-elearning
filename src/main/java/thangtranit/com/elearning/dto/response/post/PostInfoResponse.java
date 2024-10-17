package thangtranit.com.elearning.dto.response.post;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostInfoResponse {
    String id;
    String title;
    LocalDateTime createdAt;
    UserMinimumInfoResponse creator;
    List<TagResponse> tags;
    int viewCount;
    int likeCount;
}
