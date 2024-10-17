package thangtranit.com.elearning.entity.post;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "post_like")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "like_id")
    String id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    User user;
    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    Post post;
}
