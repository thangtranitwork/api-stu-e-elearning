package thangtranit.com.elearning.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.user.FriendshipResponse;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;
import thangtranit.com.elearning.service.user.FriendshipService;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendshipController {
    final FriendshipService friendshipService;
    @GetMapping("/search")
    ApiResponse<Page<UserMinimumInfoResponse>> getFriends(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page ) {
        return ApiResponse.<Page<UserMinimumInfoResponse>>builder()
                .body(friendshipService.findFriends(name, page))
                .build();
    }
    @GetMapping("/{targetId}")
    ApiResponse<FriendshipResponse> getFriend(
            @PathVariable String targetId
    ) {
        return ApiResponse.<FriendshipResponse>builder()
                .body(friendshipService.getFriendShipResponse(targetId))
                .build();
    }
    @PostMapping("/{targetId}/add")
    ApiResponse<Void> addFriend(@PathVariable String targetId) {
        friendshipService.addFriend(targetId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{targetId}/unfriend")
    ApiResponse<Void> deleteFriend(@PathVariable String targetId) {
        friendshipService.unfriend(targetId);
        return ApiResponse.<Void>builder().build();
    }

    @PatchMapping("/{targetId}/accept")
    ApiResponse<Void> acceptFriend(@PathVariable String targetId) {
        friendshipService.acceptFriendRequest(targetId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{targetId}/cancel")
    ApiResponse<Void> cancelFriend(@PathVariable String targetId) {
        friendshipService.cancelFriendRequest(targetId);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{targetId}/reject")
    ApiResponse<Void> rejectFriend(@PathVariable String targetId) {
        friendshipService.rejectFriendRequest(targetId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/invitationReceived")
    ApiResponse<Page<UserMinimumInfoResponse>> getInvitationsReceived(@RequestParam(defaultValue = "0") int page){
        return ApiResponse.<Page<UserMinimumInfoResponse>>builder()
                .body(friendshipService.getInvitationReceived(page))
                .build();
    }

    @GetMapping("/invitationSend")
    ApiResponse<Page<UserMinimumInfoResponse>> getInvitationsSend(@RequestParam(defaultValue = "0") int page){
        return ApiResponse.<Page<UserMinimumInfoResponse>>builder()
                .body(friendshipService.getInvitationSend(page))
                .build();
    }
}
