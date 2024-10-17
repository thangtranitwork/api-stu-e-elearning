package thangtranit.com.elearning.controller.post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.post.TagResponse;
import thangtranit.com.elearning.service.post.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagController {
    final TagService tagService;
    @GetMapping("/search")
    ApiResponse<List<TagResponse>> searchPost(
            @RequestParam(defaultValue = "") String name) {
        return ApiResponse.<List<TagResponse>>builder()
                .body(tagService.findByName(name))
                .build();
    }
}
