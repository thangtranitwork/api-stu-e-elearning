package thangtranit.com.elearning.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import thangtranit.com.elearning.dto.request.user.MessageRequest;
import thangtranit.com.elearning.dto.response.user.LatestMessageResponse;
import thangtranit.com.elearning.dto.response.user.MessageResponse;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;
import thangtranit.com.elearning.entity.user.Friendship;
import thangtranit.com.elearning.entity.user.Message;
import thangtranit.com.elearning.entity.user.MessageType;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.MessageMapper;
import thangtranit.com.elearning.mapper.UserMapper;
import thangtranit.com.elearning.repository.user.MessageRepository;
import thangtranit.com.elearning.service.util.CloudinaryService;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final MessageMapper messageMapper;
    private final FriendshipService friendshipService;
    private final UserMapper userMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageResponse send(String friendshipId, MessageRequest request) {
        if (request.getContent().isEmpty()) {
            throw new AppException(ErrorCode.MESSAGE_CANNOT_BE_EMPTY);
        }


        String destination = "/user/" + friendshipId + "/private";
        Message message = saveMessage(userService.getCurrentUser(), friendshipService.getFriendship(friendshipId), MessageType.TEXT, request.getContent());
        MessageResponse response = messageMapper.toMessageResponse(message);
        messagingTemplate.convertAndSend(destination, response);
        return response;
    }

    public Message saveMessage(User sender, Friendship friendship, MessageType type, String content) {
        Message message = Message.builder()
                .sender(sender)
                .friendship(friendship)
                .content(content)
                .type(type)
                .build();
        messageRepository.save(message);
        updateLatestMessage(friendship.getId());
        String targetId = friendship.getA().getId().equals(sender.getId())
                ? friendship.getB().getId()
                : friendship.getA().getId();
        updateNotCountMessages(targetId);
        return message;
    }

    public Page<MessageResponse> getHistory(String friendshipId, int page) {
        markMessagesAsRead(friendshipId, userService.getCurrentUserId());
        updateNotCountMessages(userService.getCurrentUserId());
        return messageRepository.findAllByFriendshipIdOrderByCreatedAtDesc(friendshipId, PageRequest.of(page, 20)).map(messageMapper::toMessageResponse);
    }

    public MessageResponse sendImage(MultipartFile file, String friendshipId) {
        String url = cloudinaryService.upload(file, Map.of());
        Message message = saveMessage(userService.getCurrentUser(), friendshipService.getFriendship(friendshipId), MessageType.IMAGE, url);

        MessageResponse response = messageMapper.toMessageResponse(messageRepository.save(message));
        String destination = "/user/" + friendshipId + "/private";
        messagingTemplate.convertAndSend(destination, response);
        return response;
    }

    private void markMessagesAsRead(String friendshipId, String userId) {
        messageRepository.markAsRead(friendshipId, userId);
    }

    public int countNotReadMessages(String friendshipId, String userId) {
        return messageRepository.countNotReadMessages(friendshipId, userId);
    }

    public int countNotReadMessages() {
        int counted = messageRepository.countNotReadMessages(userService.getCurrentUserId());
        String destination = "/user/" + userService.getCurrentUserId() + "/chat/count";
        messagingTemplate.convertAndSend(destination, counted);
        return counted;
    }

    private void updateNotCountMessages(String userId) {
        int counted = messageRepository.countNotReadMessages(userId);
        String destination = "/user/" + userId + "/chat/count";
        messagingTemplate.convertAndSend(destination, counted);
    }

    public Optional<Message> getLatestMessage(String friendshipId) {
        return messageRepository.findTop1ByFriendshipIdOrderByCreatedAtDesc(friendshipId);
    }

    public Page<LatestMessageResponse> getChatList(String name, int page) {
        Page<UserMinimumInfoResponse> responses = friendshipService.findFriends(name, page);
        return responses.map(this::getLatestMessageResponse);
    }

    public LatestMessageResponse getLatestMessageResponse(UserMinimumInfoResponse target) {
        String currentUserId = userService.getCurrentUserId();
        String a = friendshipService.sortUser(currentUserId, target.getId()).getLeft();
        String b = friendshipService.sortUser(currentUserId, target.getId()).getRight();
        String id = friendshipService.findExistingFriendship(a, b).getId();
        return LatestMessageResponse.builder()
                .friend(target)
                .message(getLatestMessage(id).map(messageMapper::toMessageResponse).orElse(null))
                .notReadMessagesCount(countNotReadMessages(id, currentUserId))
                .build();
    }

    public void updateLatestMessage(String friendshipId) {
        Friendship friendship = friendshipService.getFriendship(friendshipId);
        String currentUserId = userService.getCurrentUserId();
        String targetId = friendship.getA().getId().equals(currentUserId)
                ? friendship.getB().getId()
                : friendship.getA().getId();
        LatestMessageResponse response = LatestMessageResponse.builder()
                .friend(userMapper.toUserMinimumInfoResponse(userService.getCurrentUser()))
                .message(getLatestMessage(friendshipId).map(messageMapper::toMessageResponse).orElse(null))
                .notReadMessagesCount(countNotReadMessages(friendshipId, targetId))
                .build();
        String destination = "/user/" + targetId + "/chat";
        messagingTemplate.convertAndSend(destination, response);
    }
}

