package thangtranit.com.elearning.service.post;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.post.CommentRequest;
import thangtranit.com.elearning.dto.response.post.CommentResponse;
import thangtranit.com.elearning.entity.post.Comment;
import thangtranit.com.elearning.entity.post.Post;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.CommentMapper;
import thangtranit.com.elearning.repository.post.CommentRepository;
import thangtranit.com.elearning.service.user.UserService;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final CommentMapper commentMapper;
    @NonFinal
    private final int PAGE_SIZE = 10;

    public Comment getComment(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_IS_NOT_EXISTS));
    }

    public CommentResponse comment(String postId, CommentRequest request) {
        Post post = postService.getPost(postId);
        Comment comment = Comment.builder()
                .content(request.getContent())
                .creator(userService.getCurrentUser())
                .post(post)
                .build();
        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    public Page<CommentResponse> getComments(String postId, int page) {
        return commentRepository
                .findByPostIdOrderByCreatedAtDesc(postId, PageRequest.of(page, PAGE_SIZE))
                .map(commentMapper::toCommentResponse);
    }

    public Page<CommentResponse> getUsersComments(String postId, int page) {
        String userId = userService.getCurrentUserId();
        return commentRepository
                .findByPostIdAndCreatorIdOrderByCreatedAtDesc(postId, userId, PageRequest.of(page, PAGE_SIZE))
                .map(commentMapper::toCommentResponse);
    }

    @PreAuthorize("@commentService.getComment(#id).creator.id == @userService.getCurrentUserId()")
    public void delete(String postId, String commentId) {
        if (getComment(commentId).getPost().getId().equals(postId))
            throw new AppException(ErrorCode.THE_COMMENT_DOES_NOT_BELONG_TO_THE_POST);
        commentRepository.deleteById(commentId);
    }

}
