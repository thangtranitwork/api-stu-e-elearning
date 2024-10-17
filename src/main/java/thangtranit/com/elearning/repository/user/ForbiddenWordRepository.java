package thangtranit.com.elearning.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import thangtranit.com.elearning.entity.user.ForbiddenWord;

import java.util.Optional;

public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord, String> {
    Page<ForbiddenWord> findAllByWordContainingIgnoreCaseOrderByWord(String word, Pageable pageable);
    Optional<ForbiddenWord> findByWord(String word);
}