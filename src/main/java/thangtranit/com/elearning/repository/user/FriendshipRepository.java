package thangtranit.com.elearning.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.user.Friendship;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, String> {
    Optional<Friendship> findByA_IdAndB_Id(String A_Id, String B_Id);
    @Query("""
    SELECT f
    FROM Friendship f
    LEFT JOIN Message m ON m.friendship.id = f.id
    WHERE f.accepted = true
    AND (
        (f.a.id = :userId AND LOWER(CONCAT(f.b.firstname, ' ', f.b.lastname)) LIKE LOWER(CONCAT('%', :name, '%')))
        OR
        (f.b.id = :userId AND LOWER(CONCAT(f.a.firstname, ' ', f.a.lastname)) LIKE LOWER(CONCAT('%', :name, '%')))
    )
    GROUP BY f.id
    ORDER BY MAX(m.createdAt) DESC
""")

    Page<Friendship> findFriendshipByName(@Param("userId") String userId, @Param("name") String targetName, Pageable pageable);
    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE (f.a.id = :userId OR f.b.id = :userId) AND f.sender.id <> :userId AND f.accepted = false " +
            "ORDER BY f.createdAt")
    Page<Friendship> findInvitationReceived(String userId, Pageable pageable);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE (f.a.id = :userId OR f.b.id = :userId) AND f.sender.id = :userId AND f.accepted = false " +
            "ORDER BY f.createdAt")
    Page<Friendship> findInvitationSend(String userId, Pageable pageable);


}
