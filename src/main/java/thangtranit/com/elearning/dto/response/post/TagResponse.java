package thangtranit.com.elearning.dto.response.post;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagResponse {
    String id;
    String name;
}
