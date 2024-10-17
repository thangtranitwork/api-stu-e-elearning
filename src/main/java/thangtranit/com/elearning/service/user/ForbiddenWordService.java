package thangtranit.com.elearning.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.entity.user.ForbiddenWord;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.repository.user.ForbiddenWordRepository;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ForbiddenWordService {
    private final ForbiddenWordRepository forbiddenWordRepository;
    private int pageSize = 5;

    public Page<ForbiddenWord> getForbiddenWords(String word, int page) {
        return forbiddenWordRepository.findAllByWordContainingIgnoreCaseOrderByWord(word, PageRequest.of(page, pageSize));
    }

    private ForbiddenWord getForbiddenWord(String id) {
        return forbiddenWordRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.FORBIDDEN_WORD_NOT_FOUND));
    }

    public void delete(String id) {
        forbiddenWordRepository.delete(getForbiddenWord(id));
    }

    public ForbiddenWord create(String word) {
        if (forbiddenWordRepository.findByWord(word).isPresent())
            throw new AppException(ErrorCode.FORBIDDEN_WORD_HAS_ALREADY_EXISTS);
        ForbiddenWord forbiddenWord = new ForbiddenWord();
        forbiddenWord.setWord(word);
        return forbiddenWordRepository.save(forbiddenWord);
    }

}
