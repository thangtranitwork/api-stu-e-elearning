package thangtranit.com.elearning.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import thangtranit.com.elearning.dto.request.user.MessageRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.user.LatestMessageResponse;
import thangtranit.com.elearning.dto.response.user.MessageResponse;
import thangtranit.com.elearning.service.user.ChatService;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    final ChatService chatService;
    @PostMapping("/{friendshipId}")
    ApiResponse<MessageResponse> processMessage(@PathVariable String friendshipId, @RequestBody MessageRequest message) {
        MessageResponse response = chatService.send(friendshipId, message);
        return ApiResponse.<MessageResponse>builder()
                .body(response)
                .build();
    }

    @RequestMapping("{friendshipId}/history")
    ApiResponse<Page<MessageResponse>> history(@PathVariable String friendshipId, @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<MessageResponse>>builder()
                .body(chatService.getHistory(friendshipId, page))
                .build();
    }

    @PostMapping("/image")
    ApiResponse<MessageResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("friendshipId") String friendshipId
    ) {
        MessageResponse response = chatService.sendImage(file, friendshipId);
        return ApiResponse.<MessageResponse>builder()
                .body(response)
                .build();
    }

    @GetMapping("/notReadMessagesCount")
    ApiResponse<Integer> countNotReadMessages() {
        return ApiResponse.<Integer>builder()
                .body(chatService.countNotReadMessages())
                .build();
    }

    @GetMapping("/search")
    ApiResponse<Page<LatestMessageResponse>> getChatList(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<LatestMessageResponse>>builder()
                .body(chatService.getChatList(name, page))
                .build();
    }
}