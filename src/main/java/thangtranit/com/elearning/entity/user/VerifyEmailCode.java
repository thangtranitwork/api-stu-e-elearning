package thangtranit.com.elearning.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyEmailCode {
    @Id
    String code;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    Date expiryDate;
}
