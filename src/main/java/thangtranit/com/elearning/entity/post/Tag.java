package thangtranit.com.elearning.entity.post;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tag_id")
    String id;

    @Column(nullable = false, unique = true)
    String name;

    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    @Builder.Default
    List<Post> posts = new ArrayList<>();

    // Override equals and hashCode based on `name` to avoid issues with unique constraint
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

