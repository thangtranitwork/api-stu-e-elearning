package thangtranit.com.elearning.dto.request.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.validator.ForbiddenWords;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileRequest {
    @ForbiddenWords
    String firstname;
    @ForbiddenWords
    String lastname;
    LocalDate birthday;
    @ForbiddenWords
    String address;
    @ForbiddenWords
    String bio;
}
