package thangtranit.com.elearning.service.post;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.post.PostRequest;
import thangtranit.com.elearning.dto.response.post.PostInfoResponse;
import thangtranit.com.elearning.dto.response.post.PostResponse;
import thangtranit.com.elearning.entity.post.Like;
import thangtranit.com.elearning.entity.post.Post;
import thangtranit.com.elearning.entity.post.View;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.PostMapper;
import thangtranit.com.elearning.repository.post.LikeRepository;
import thangtranit.com.elearning.repository.post.PostRepository;
import thangtranit.com.elearning.repository.post.ViewRepository;
import thangtranit.com.elearning.service.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final ViewRepository viewRepository;
    private final LikeRepository likeRepository;
    @NonFinal
    private final int pageSize = 10;

    public Post getPost(String id) {
        return postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_IS_NOT_EXISTS));
    }

    public PostResponse getPostResponse(String id) {
        PostResponse response = postMapper.toPostResponse(getPost(id));
        if (!userService.isAnonymous()) {
            view(id);
            likeRepository.findByPostIdAndUserId(id,
                            userService.getCurrentUserId())
                        .ifPresent(like -> response.setLiked(true));
        }
    return response;
    }
    public Page<PostInfoResponse> searchPosts(String name, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return postRepository.findAllByTitleContainingOrderByCreatedAtDesc(name, pageable).map(postMapper::toPostInfoResponse);
    }

    public Page<PostInfoResponse> searchPosts(List<String> tags, int page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return postRepository.findByAnyTag(tags, pageable).map(postMapper::toPostInfoResponse);
    }

    public List<PostInfoResponse> getHottestPost(){
        return postRepository.findTop3OrderByLikesCountDesc().stream().map(postMapper::toPostInfoResponse).toList();
    }

    public PostInfoResponse create(PostRequest request) {
        Post post = postMapper.toPost(request);
        post.setCreator(userService.getCurrentUser());
        return postMapper.toPostInfoResponse(postRepository.save(post));
    }

    @PreAuthorize("@postService.isOwner(#postId)")
    public void delete(String postId) {
        postRepository.deleteById(postId);
    }

    public boolean isOwner(String postId) {
        return getPost(postId).getCreator().getId().equals(userService.getCurrentUser().getId());
    }

    public void view(String postId) {
        if (!userService.isAnonymous() && !isOwner(postId)) {
            Optional<View> view = viewRepository.findByPostIdAndViewerId(postId, userService.getCurrentUserId());
            if (view.isEmpty()) {
                viewRepository.save(View.builder()
                        .post(getPost(postId))
                        .viewer(userService.getCurrentUser())
                        .build());
            }
        }
    }

    public int like(String postId) {
        Post post = getPost(postId);
        Optional<Like> like = likeRepository.findByPostIdAndUserId(postId, userService.getCurrentUserId());
        if (like.isEmpty()) {
            likeRepository.save(Like.builder()
                    .post(post)
                    .user(userService.getCurrentUser())
                    .build());
            }
        return post.getLikes().size();
    }

    public int unlike(String postId) {
        Post post = getPost(postId);
        likeRepository.findByPostIdAndUserId(postId, userService.getCurrentUserId())
                    .ifPresent(likeRepository::delete);
        return post.getLikes().size();
    }

    public Page<PostInfoResponse> getLikedPosts(int page){
        return postRepository
                .findLikedPostOrderByCreatedAtDesc(userService.getCurrentUserId(), PageRequest.of(page, pageSize))
                .map(postMapper::toPostInfoResponse);
    }

    public Page<PostInfoResponse> getCreatedPosts(int page){
        Page<Post> allByCreatorIdOrderByCreatedAtDesc = postRepository
                .findAllByCreatorIdOrderByCreatedAtDesc(userService.getCurrentUserId(), PageRequest.of(page, pageSize));
        return allByCreatorIdOrderByCreatedAtDesc
                .map(postMapper::toPostInfoResponse);
    }
}
