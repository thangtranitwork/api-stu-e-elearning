package thangtranit.com.elearning.dto.request.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.validator.ForbiddenWords;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRequest {
    @ForbiddenWords
    @NotEmpty(message = "MESSAGE_CANNOT_BE_EMPTY")
    String content;
}
