package thangtranit.com.elearning.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.post.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    Page<Post> findAllByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE t.id IN :tags ORDER BY p.createdAt DESC")
    Page<Post> findByAnyTag(@Param("tags") List<String> tags, Pageable pageable);
    @Query("SELECT p FROM Post p ORDER BY SIZE(p.likes) DESC LIMIT 3")
    List<Post> findTop3OrderByLikesCountDesc();
    @Query("SELECT p FROM Post p " +
            "JOIN p.likes l " +
            "WHERE l.user.id = :userId " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findLikedPostOrderByCreatedAtDesc(String userId, Pageable pageable);
    Page<Post> findAllByCreatorIdOrderByCreatedAtDesc(String creatorId, Pageable pageable);
}
