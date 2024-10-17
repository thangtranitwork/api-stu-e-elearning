package thangtranit.com.elearning.dto.request.user;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpVerifyRequest {
    @Size(min = 6, message = "OTP_INVALID")
    String otp;
}
