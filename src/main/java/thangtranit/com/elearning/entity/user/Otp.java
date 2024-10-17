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
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    User user;

    String otp;
    Date expiryDate;
    @Builder.Default
    int remaining = 5;
    @Builder.Default
    boolean isVerified = false;
}
