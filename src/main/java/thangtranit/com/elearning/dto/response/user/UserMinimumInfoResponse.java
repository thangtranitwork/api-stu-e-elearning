package thangtranit.com.elearning.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMinimumInfoResponse {
    String id;
    @Builder.Default
    String lastname = "";
    @Builder.Default
    String firstname = "";
    @Builder.Default
    String avatar = "";
    LocalDateTime lastOnline;
}
