package thangtranit.com.elearning.dto.request.user;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 8, message = "INVALID_NEW_PASSWORD")
    String newPassword;
}
