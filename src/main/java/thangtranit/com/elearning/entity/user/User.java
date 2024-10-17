package thangtranit.com.elearning.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.course.Course;
import thangtranit.com.elearning.entity.course.Enrollment;
import thangtranit.com.elearning.entity.post.Comment;
import thangtranit.com.elearning.entity.post.Like;
import thangtranit.com.elearning.entity.post.Post;
import thangtranit.com.elearning.entity.quiz.AnswerQuiz;
import thangtranit.com.elearning.entity.quiz.Quiz;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(name = "unique_email_platform", columnNames = {"email", "platform"})}
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    String id;
    String firstname;
    String lastname;
    LocalDate birthday;
    String address;
    String bio;
    String avatar;
    @Column(nullable = false)
    String email;
    String password;
    String roles;
    @Enumerated(EnumType.STRING)
    Platform platform;
    LocalDateTime lastOnline;
    boolean isVerified;
    boolean accountLocked;
    int failedAttempts;
    LocalDateTime lockoutTime;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    Otp otp;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    VerifyEmailCode verifyEmailCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<AnswerQuiz> playedQuizzes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Quiz> createdQuizzes;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Course> createdCourses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Enrollment> enrollments;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Like> likes;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Comment> comments;

}
