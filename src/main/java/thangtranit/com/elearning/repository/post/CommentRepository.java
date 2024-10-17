package thangtranit.com.elearning.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.post.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findByPostIdOrderByCreatedAtDesc(String postId, Pageable pageable);
    Page<Comment> findByPostIdAndCreatorIdOrderByCreatedAtDesc(String postId, String creator, Pageable pageable);
}
