package thangtranit.com.elearning.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    @Builder.Default
    String lastname = "";
    @Builder.Default
    String firstname = "";
    @Builder.Default
    String avatar = "";
    LocalDate birthday;
    @Builder.Default
    String address = "";
    @Builder.Default
    String bio = "";
    boolean isFriend;
    @Builder.Default
    int addFriendRequestSent = 0;
    LocalDateTime lastOnline;
}
