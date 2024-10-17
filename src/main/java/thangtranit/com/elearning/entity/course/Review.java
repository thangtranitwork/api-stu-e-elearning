package thangtranit.com.elearning.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "course_id"})
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id")
    String id;

    @Column(name = "star", nullable = false)
    byte star;

    @Column(name = "text", nullable = false)
    String text;

    @Builder.Default
    @Column(name = "created_time", nullable = false)
    LocalDateTime createdTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    User reviewer;
}
