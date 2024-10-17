package thangtranit.com.elearning.controller.post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.post.CommentRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.post.CommentResponse;
import thangtranit.com.elearning.service.post.CommentService;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentController {
    final CommentService commentService;
    final SimpMessagingTemplate messagingTemplate;
    @PostMapping("/new")
    ApiResponse<CommentResponse> comment(@PathVariable String postId, @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = commentService.comment(postId, commentRequest);
        messagingTemplate.convertAndSend("/post/" + postId + "/comment", comment);
        return ApiResponse.<CommentResponse>builder()
                .body(comment)
                .build();
    }

    @GetMapping("")
    ApiResponse<Page<CommentResponse>> getAll(@PathVariable String postId, @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<CommentResponse>>builder()
                .body(commentService.getComments(postId, page))
                .build();
    }

    @GetMapping("/own")
    ApiResponse<Page<CommentResponse>> getOwn(@PathVariable String postId, @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<CommentResponse>>builder()
                .body(commentService.getUsersComments(postId, page))
                .build();
    }

    @DeleteMapping("/delete")
    ApiResponse<Void> delete(@PathVariable String postId, @RequestParam String commentId) {
        commentService.delete(postId, commentId);
        return ApiResponse.<Void>builder()
                .build();
    }
}
