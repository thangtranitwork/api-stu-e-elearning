package thangtranit.com.elearning.dto.request.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.validator.ForbiddenWords;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {
    @Size(min = 5, max = 255, message = "TITLE_LENGTH_IS_INVALID")
    @ForbiddenWords
    String title;
    @Size(min = 50, message = "POST_CONTENT_MUST_HAVE_AT_LEAST_50_CHARACTER")
    @ForbiddenWords
    String content;
    @NotNull(message = "POST_MUST_HAVE_AT_LEAST_ONE_TAG")
    @Size(min = 1, message = "POST_MUST_HAVE_AT_LEAST_ONE_TAG")
    List<String> tags;
}
