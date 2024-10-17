package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import thangtranit.com.elearning.dto.response.user.FriendshipResponse;
import thangtranit.com.elearning.entity.user.Friendship;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public abstract class FriendshipMapper {
    public abstract FriendshipResponse toFriendshipResponse(Friendship friendship);
}
