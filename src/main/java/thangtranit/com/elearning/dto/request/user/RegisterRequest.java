package thangtranit.com.elearning.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.Platform;
import thangtranit.com.elearning.entity.user.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @Email(message = "INVALID_EMAIL")
    String email;
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    @Builder.Default
    String roles = Role.USER.name();
    @Builder.Default
    String platform = Platform.APP.name();
}
