package thangtranit.com.elearning.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.user.Platform;
import thangtranit.com.elearning.entity.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndPlatform(String email, Platform platform);
    boolean existsByEmailAndPlatform(String email, Platform platform);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(CONCAT(u.firstname, ' ', u.lastname)) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> findByNameContaining(String name, Pageable pageable);
}
