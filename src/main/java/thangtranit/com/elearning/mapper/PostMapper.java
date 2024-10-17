package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import thangtranit.com.elearning.dto.request.post.PostRequest;
import thangtranit.com.elearning.dto.response.post.PostInfoResponse;
import thangtranit.com.elearning.dto.response.post.PostResponse;
import thangtranit.com.elearning.entity.post.Post;
import thangtranit.com.elearning.entity.post.Tag;
import thangtranit.com.elearning.repository.post.TagRepository;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class PostMapper {
    @Autowired
    protected TagRepository tagRepository;

    @Mapping(target = "viewCount", expression = "java(mapViewCount(post))")
    @Mapping(target = "likeCount", expression = "java(mapLikeCount(post))")
    public abstract PostInfoResponse toPostInfoResponse(Post post);

    @Mapping(target = "tags", expression = "java(mapTags(request))")
    public abstract Post toPost(PostRequest request);

    @Mapping(target = "likeCount", expression = "java(mapLikeCount(post))")
    public abstract PostResponse toPostResponse(Post post);


    protected int mapViewCount(Post post) {
        return post.getViews().size();
    }

    protected int mapLikeCount(Post post) {
        return post.getLikes().size();
    }

    protected List<Tag> mapTags(PostRequest request) {
        List<Tag> tags = new ArrayList<>();

        request.getTags().forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .name(tagName)
                                .build();
                        // Lưu tag mới vào repository
                        return tagRepository.save(newTag);
                    });
            // Thêm tag đã tìm thấy hoặc mới tạo vào danh sách
            tags.add(tag);
        });

        return tags;
    }

}
