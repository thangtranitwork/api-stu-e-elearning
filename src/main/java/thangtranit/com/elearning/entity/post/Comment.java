package thangtranit.com.elearning.entity.post;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "post_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comment_id")
    String id;
    @Column(nullable = false)
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    String content;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    Post post;
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    User creator;
}
