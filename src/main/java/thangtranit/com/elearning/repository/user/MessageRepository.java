package thangtranit.com.elearning.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thangtranit.com.elearning.entity.user.Message;

import java.util.Optional;

@Repository

public interface MessageRepository extends JpaRepository<Message, String> {
        Page<Message> findAllByFriendshipIdOrderByCreatedAtDesc(String id,  Pageable pageable);
        @Query("SELECT COUNT (m.id) FROM Message m " +
                "WHERE m.friendship.id = :friendshipId AND m.sender.id <> :userId AND m.isRead = false")
        int countNotReadMessages(String friendshipId, String userId);
        @Query("SELECT COUNT(m.id) FROM Message m " +
                "WHERE m.sender.id <> :userId " +
                "AND (m.friendship.a.id = :userId OR m.friendship.b.id = :userId) " +
                "AND m.isRead = false")
        int countNotReadMessages(String userId);
        @Modifying
        @Transactional
        @Query("UPDATE Message m SET m.isRead = true " +
                "WHERE m.friendship.id = :friendshipId AND m.sender.id <> :userId AND m.isRead = false")
        void markAsRead( String friendshipId, String userId);

        Optional<Message> findTop1ByFriendshipIdOrderByCreatedAtDesc(String friendshipId);

}
