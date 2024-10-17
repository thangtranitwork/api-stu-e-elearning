package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import thangtranit.com.elearning.dto.response.post.CommentResponse;
import thangtranit.com.elearning.entity.post.Comment;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class CommentMapper {
    public abstract CommentResponse toCommentResponse(Comment comment);
}
