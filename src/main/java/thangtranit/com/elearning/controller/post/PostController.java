package thangtranit.com.elearning.controller.post;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.post.PostRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.post.PostInfoResponse;
import thangtranit.com.elearning.dto.response.post.PostResponse;
import thangtranit.com.elearning.service.post.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostController {
    final PostService postService;
    final SimpMessagingTemplate messagingTemplate;
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> get(@PathVariable String postId) {
        return ApiResponse.<PostResponse>builder()
                .body(postService.getPostResponse(postId))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<Page<PostInfoResponse>> searchPost(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<PostInfoResponse>>builder()
                .body(postService.searchPosts(name, page))
                .build();
    }

    @GetMapping("/search_by_tags")
    ApiResponse<Page<PostInfoResponse>> searchPost(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<PostInfoResponse>>builder()
                .body(postService.searchPosts(tags, page))
                .build();
    }

    @GetMapping("/liked")
    ApiResponse<Page<PostInfoResponse>> getLikedPosts(
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<PostInfoResponse>>builder()
                .body(postService.getLikedPosts(page))
                .build();
    }

    @GetMapping("/created")
    ApiResponse<Page<PostInfoResponse>> getCreatedPosts(
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.<Page<PostInfoResponse>>builder()
                .body(postService.getCreatedPosts(page))
                .build();
    }

    @GetMapping("/hottest")
    ApiResponse<List<PostInfoResponse>> getHottestPost() {
        return ApiResponse.<List<PostInfoResponse>>builder()
                .body(postService.getHottestPost())
                .build();
    }
    @PostMapping("/new")
    ApiResponse<PostInfoResponse> post(@RequestBody @Valid PostRequest request) {
        PostInfoResponse response = postService.create(request);
        messagingTemplate.convertAndSend("/post" , response);
        return ApiResponse.<PostInfoResponse>builder()
                .body(response)
                .build();
    }


    @PostMapping("/{post_id}/like")
    ApiResponse<Void> like(@PathVariable(name = "post_id") String postId){
        int likes = postService.like(postId);
        messagingTemplate.convertAndSend("/post/" + postId + "/like", likes);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{post_id}/unlike")
    ApiResponse<Void> unlike(@PathVariable(name = "post_id") String postId){
        int likes = postService.unlike(postId);
        messagingTemplate.convertAndSend("/post/" + postId + "/like", likes);
        return ApiResponse.<Void>builder().build();
    }

    @DeleteMapping("/{post_id}/delete")
    ApiResponse<Void> delete(@PathVariable(name = "post_id") String postId) {
        postService.delete(postId);
        return ApiResponse.<Void>builder().build();
    }

}
