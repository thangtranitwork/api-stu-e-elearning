package thangtranit.com.elearning.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.post.View;

import java.util.Optional;

@Repository
public interface ViewRepository extends JpaRepository<View, String> {
    Optional<View> findByPostIdAndViewerId(String postId, String viewerId);
}
