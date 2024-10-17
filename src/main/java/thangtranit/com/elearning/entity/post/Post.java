package thangtranit.com.elearning.entity.post;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import thangtranit.com.elearning.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    String id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @Column(nullable = false)
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id", nullable = false)
    @ToString.Exclude
    User creator;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "tag_id"})

    )
    @ToString.Exclude
    @Builder.Default
    List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    List<View> views = new ArrayList<>();
}
