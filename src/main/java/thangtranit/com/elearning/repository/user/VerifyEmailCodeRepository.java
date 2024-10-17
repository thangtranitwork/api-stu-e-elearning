package thangtranit.com.elearning.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thangtranit.com.elearning.entity.user.VerifyEmailCode;

import java.util.Optional;

@Repository
public interface VerifyEmailCodeRepository extends JpaRepository<VerifyEmailCode, String> {
    Optional<VerifyEmailCode> findByUserEmail(String email);
}
