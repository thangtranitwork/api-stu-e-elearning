package thangtranit.com.elearning.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LatestMessageResponse {
    @Builder.Default
    int notReadMessagesCount = 0;
    MessageResponse message;
    UserMinimumInfoResponse friend;
}
