package thangtranit.com.elearning.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAdminViewResponse {
    String id;
    @Builder.Default
    String lastname = "";
    @Builder.Default
    String firstname = "";
    @Builder.Default
    String avatar = "";
    @Builder.Default
    String roles = "";
}
