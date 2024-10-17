package thangtranit.com.elearning.service.user;

import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.response.user.FriendshipResponse;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;
import thangtranit.com.elearning.entity.user.Friendship;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.FriendshipMapper;
import thangtranit.com.elearning.mapper.UserMapper;
import thangtranit.com.elearning.repository.user.FriendshipRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final FriendshipMapper friendshipMapper;
    @NonFinal
    private final int pageSize = 10;

    public Page<UserMinimumInfoResponse> findFriends(String name, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        String id = userService.getCurrentUserId();
        return friendshipRepository.findFriendshipByName(id, name, pageable)
                .map(friendship ->
                        userMapper.toUserMinimumInfoResponse(
                                friendship.getA().getId().equals(id)
                                        ? friendship.getB()
                                        : friendship.getA()));
    }

    // Tạo lời mời kết bạn
    public void addFriend(String targetId) {
        String currentUserId = userService.getCurrentUserId();
        String a = sortUser(currentUserId, targetId).getLeft();
        String b = sortUser(currentUserId, targetId).getRight();

        // Kiểm tra xem lời mời kết bạn đã tồn tại chưa
        Optional<Friendship> existingFriendship = findFriendship(a, b);

        validateAddFriendRequest(a, b);
        if (existingFriendship.isPresent()) {
            Friendship friendship = existingFriendship.get();

            // Kiểm tra nếu đã là bạn
            if (friendship.isAccepted()) {
                throw new AppException(ErrorCode.HAVE_ALREADY_BEEN_FRIEND);
            }

            // Kiểm tra nếu lời mời kết bạn đã được gửi
            if (friendship.getSender().getId().equals(currentUserId)) {
                throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_ALREADY_SENT);
            } else {
                friendship.setAccepted(true);
                friendshipRepository.save(friendship);
            }

        } else {
            // Tạo lời mời kết bạn mới
            Friendship newFriendship = Friendship.builder()
                    .a(userService.getUser(a))
                    .b(userService.getUser(b))
                    .sender(userService.getCurrentUser()) // Gán người gửi là currentUser
                    .build();
            friendshipRepository.save(newFriendship);
        }
    }

    // Hủy kết bạn
    public void unfriend(String targetId) {
        String currentUserId = userService.getCurrentUserId();
        String a = sortUser(currentUserId, targetId).getLeft();
        String b = sortUser(currentUserId, targetId).getRight();

        validateUnfriendRequest(a, b); // Kiểm tra điều kiện hủy kết bạn

        // Tìm kiếm mối quan hệ bạn bè
        Friendship friendship = findExistingFriendship(a, b);

        // Kiểm tra và xóa quan hệ
        if (friendship.isAccepted()) {
            friendshipRepository.delete(friendship);
        } else {
            // Chỉ người gửi lời mời mới có thể hủy lời mời
            if (friendship.getSender().getId().equals(currentUserId)) {
                friendshipRepository.delete(friendship);
            } else {
                throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND);
            }
        }
    }

    // Chấp nhận lời mời kết bạn
    public void acceptFriendRequest(String targetId) {
        String currentUserId = userService.getCurrentUserId();
        String a = sortUser(currentUserId, targetId).getLeft();
        String b = sortUser(currentUserId, targetId).getRight();

        // Tìm kiếm mối quan hệ bạn bè
        Friendship friendship = findExistingFriendship(a, b);

        // Chỉ người nhận lời mời mới có thể chấp nhận
        if (!friendship.getSender().getId().equals(currentUserId)) {
            friendship.setAccepted(true);
            friendshipRepository.save(friendship);
        } else {
            throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND);
        }
    }

    // Hủy lời mời kết bạn
    public void cancelFriendRequest(String targetId) {
        String currentUserId = userService.getCurrentUserId();
        String a = sortUser(currentUserId, targetId).getLeft();
        String b = sortUser(currentUserId, targetId).getRight();

        // Tìm kiếm mối quan hệ bạn bè
        Friendship friendship = findExistingFriendship(a, b);

        // Chỉ người gửi mới có thể hủy lời mời kết bạn
        if (friendship.getSender().getId().equals(currentUserId) && !friendship.isAccepted()) {
            friendshipRepository.delete(friendship);
        } else {
            throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND);
        }
    }

    public void rejectFriendRequest(String targetId) {
        String currentUserId = userService.getCurrentUserId();
        String a = sortUser(currentUserId, targetId).getLeft();
        String b = sortUser(currentUserId, targetId).getRight();

        // Kiểm tra nếu người dùng muốn từ chối lời mời kết bạn của chính mình
        validateRejectAddFriendRequest(a, b);


        // Kiểm tra mối quan hệ giữa hai người dùng
        Optional<Friendship> friendshipOptional = findFriendship(a, b);

        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();

            // Nếu lời mời chưa được chấp nhận
            if (!friendship.isAccepted()) {
                // Kiểm tra người gửi lời mời có phải là targetUser không
                if (!friendship.getSender().getId().equals(currentUserId)) {
                    // Xóa lời mời kết bạn
                    friendshipRepository.delete(friendship);
                } else {
                    throw new AppException(ErrorCode.CAN_NOT_REJECT_OWN_INVITATION);
                }
            } else {
                throw new AppException(ErrorCode.HAVE_ALREADY_BEEN_FRIEND);
            }
        } else {
            throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND);
        }
    }


    public boolean areFriends(String a, String b) {

        Optional<Friendship> f = friendshipRepository.findByA_IdAndB_Id(a, b);
        Optional<Friendship> f2 = friendshipRepository.findByA_IdAndB_Id(b, a);
        return (f.isPresent() && f.get().isAccepted()) || (f2.isPresent() && f2.get().isAccepted());
    }

    public Pair<String, String> sortUser(String a, String b) {
        if(a.compareTo(b) < 0){
            return Pair.of(a, b);
        }else if(a.compareTo(b) > 0){
            return Pair.of(b, a);
        }else{
            throw new AppException(ErrorCode.CAN_NOT_BE_FRIEND_WITH_YOURSELF);
        }
    }

    private Optional<Friendship> findFriendship(String a, String b) {
        return friendshipRepository.findByA_IdAndB_Id(a, b);
    }

    public Friendship findExistingFriendship(String a, String b) {
        return friendshipRepository.findByA_IdAndB_Id(a, b).orElseThrow(() -> new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND));
    }

    private void validateAddFriendRequest(String a, String b) {
        if (a.equals(b)) {
            throw new AppException(ErrorCode.CAN_NOT_SEND_ADD_FRIEND_INVITATION_TO_YOURSELF);
        }
    }

    private void validateUnfriendRequest(String a, String b) {
        if (a.equals(b)) {
            throw new AppException(ErrorCode.CAN_NOT_UNFRIEND_YOURSELF);
        }
    }

    private void validateRejectAddFriendRequest(String a, String b) {
        if (a.equals(b)) {
            throw new AppException(ErrorCode.CAN_NOT_REJECT_YOURSELF);
        }
    }


    public FriendshipResponse getFriendShipResponse(String targetId) {
        String currentUserId = userService.getCurrentUserId();
        String a = sortUser(currentUserId, targetId).getLeft();
        String b = sortUser(currentUserId, targetId).getRight();

        return friendshipMapper.toFriendshipResponse(findExistingFriendship(a, b));
    }

    public Friendship getFriendship(String id){
        return friendshipRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.IS_NOT_FRIEND));
    }

    public Page<UserMinimumInfoResponse> getInvitationReceived(int page){
        String userId = userService.getCurrentUserId();
        return friendshipRepository
                .findInvitationReceived(userId, PageRequest.of(page, pageSize))
                .map(friendship ->
                        userMapper.toUserMinimumInfoResponse(
                                friendship.getA().getId().equals(userId)
                                        ? friendship.getB()
                                        : friendship.getA()));
    }

    public Page<UserMinimumInfoResponse> getInvitationSend(int page){
        String userId = userService.getCurrentUserId();
        return friendshipRepository
                .findInvitationSend(userId, PageRequest.of(page, pageSize))
                .map(friendship ->
                        userMapper.toUserMinimumInfoResponse(
                                friendship.getA().getId().equals(userId)
                                        ? friendship.getB()
                                        : friendship.getA()));
    }
}
