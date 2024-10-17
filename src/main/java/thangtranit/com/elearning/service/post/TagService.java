package thangtranit.com.elearning.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.response.post.TagResponse;
import thangtranit.com.elearning.mapper.TagMapper;
import thangtranit.com.elearning.repository.post.TagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagResponse> findByName(String name) {
        return tagRepository.findTop3ByNameContaining(name)
                .stream().map(tagMapper::toTagResponse)
                .toList();
    }
}
