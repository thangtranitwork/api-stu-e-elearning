package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import thangtranit.com.elearning.dto.response.user.MessageResponse;
import thangtranit.com.elearning.entity.user.Message;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public abstract class MessageMapper{
    public abstract MessageResponse toMessageResponse(Message message);
}
