package thangtranit.com.elearning.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"a_id", "b_id"})})
public class Friendship {
    @Id
    @Column(name = "friendship_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne
    @JoinColumn(name = "a_id")
    User a;
    @ManyToOne
    @JoinColumn(name = "b_id")
    User b;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender;
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
    boolean accepted;

    @OneToMany(mappedBy = "friendship", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Message> messages;
}
