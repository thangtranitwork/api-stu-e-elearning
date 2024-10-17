package thangtranit.com.elearning.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendshipResponse {
    String id;
    UserMinimumInfoResponse a;
    UserMinimumInfoResponse b;
    boolean accepted;
}
