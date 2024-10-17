package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import thangtranit.com.elearning.dto.response.post.TagResponse;
import thangtranit.com.elearning.entity.post.Tag;

@Mapper(componentModel = "spring")
public abstract class TagMapper {
    public abstract TagResponse toTagResponse(Tag tag);
}
