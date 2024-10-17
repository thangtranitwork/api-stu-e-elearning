package thangtranit.com.elearning.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String content;
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    @Builder.Default
    MessageType type = MessageType.TEXT;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id", nullable = false)
    User sender;
    @ManyToOne
    @JoinColumn(name = "friendship_id")
    Friendship friendship;
    boolean isRead;
}
