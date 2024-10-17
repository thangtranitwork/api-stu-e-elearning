package thangtranit.com.elearning.dto.request.user;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.Platform;
import thangtranit.com.elearning.entity.user.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuth2RegisterRequest {
    @Email(message = "INVALID_EMAIL")
    String email;
    @Builder.Default
    String roles = Role.USER.name();
    @Builder.Default
    String platform = Platform.APP.name();
    String avatar;
    String lastname;
    String firstname;
}
