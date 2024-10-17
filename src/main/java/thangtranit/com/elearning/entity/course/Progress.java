package thangtranit.com.elearning.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"enrollment_id", "lesson_id"})})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "progress_id")
    String id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    @ToString.Exclude
    Enrollment enrollment;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @ToString.Exclude
    Lesson lesson;

    @Builder.Default
    @Column(name = "is_completed", nullable = false)
    boolean isCompleted = false;
}
