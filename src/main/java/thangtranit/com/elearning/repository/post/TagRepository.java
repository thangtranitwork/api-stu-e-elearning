package thangtranit.com.elearning.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.post.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findByName(String name);
    List<Tag> findTop3ByNameContaining(String name);
}
