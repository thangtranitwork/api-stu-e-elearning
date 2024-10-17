package thangtranit.com.elearning.dto.request.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.Platform;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @Email(message = "INVALID_EMAIL")
    String email;
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    String platform = Platform.APP.name();
}
