package thangtranit.com.elearning.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.post.Like;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {
    Optional<Like> findByPostIdAndUserId(String postId, String userId);
}
