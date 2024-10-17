package thangtranit.com.elearning.entity.course;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "lesson_id")
    String id;

    @Column(name = "sequence_number", nullable = false)
    int sequenceNumber;

    @Column(name = "lesson_name", nullable = false)
    String name;

    @Column(name = "theory", nullable = false, columnDefinition = "TEXT")
    String theory;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Progress> progresses;
}
