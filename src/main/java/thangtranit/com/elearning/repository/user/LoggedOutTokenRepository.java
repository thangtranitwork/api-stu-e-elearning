package thangtranit.com.elearning.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.user.LoggedOutToken;

@Repository
public interface LoggedOutTokenRepository extends JpaRepository<LoggedOutToken, String> {
}
