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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "view_id")
    String id;
    @ManyToOne
    @JoinColumn(name = "viewer_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    User viewer;
    @ManyToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    Post post;
}
