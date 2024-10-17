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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "course_id")
    String id;

    @Column(name = "course_name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "introduction", nullable = false, columnDefinition = "TEXT")

    String introduction;

    @Column(name = "price", nullable = false)
    int price;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Enrollment> enrollments;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    User creator;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Review> reviews;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    public void addLesson(Lesson lesson) {
        lesson.setCourse(this);
        this.lessons.add(lesson);
    }

}
