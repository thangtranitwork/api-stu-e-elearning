package thangtranit.com.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import thangtranit.com.elearning.dto.request.user.OAuth2RegisterRequest;
import thangtranit.com.elearning.dto.request.user.RegisterRequest;
import thangtranit.com.elearning.dto.request.user.UserProfileRequest;
import thangtranit.com.elearning.dto.response.user.UserAdminViewResponse;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;
import thangtranit.com.elearning.dto.response.user.UserProfileResponse;
import thangtranit.com.elearning.entity.user.User;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {
    User toUser(RegisterRequest request);

    User toUser(OAuth2RegisterRequest request);

    UserProfileResponse toUserProfileResponse(User user);

    UserMinimumInfoResponse toUserMinimumInfoResponse(User user);

    UserAdminViewResponse toUserAdminViewResponse(User user);

    void updateUser(@MappingTarget User user, UserProfileRequest request);
}
