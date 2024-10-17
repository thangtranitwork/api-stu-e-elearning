package thangtranit.com.elearning.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "student_id"})})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "enrollment_id")
    String id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    User student;

    @Builder.Default
    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime = LocalDateTime.now();

    @Column(name = "complete_time")
    LocalDateTime completeTime;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Progress> progresses;

    @Builder.Default
    String paymentInfo = "FREE";
}
