package thangtranit.com.elearning.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.entity.user.ForbiddenWord;
import thangtranit.com.elearning.service.user.ForbiddenWordService;

@RestController
@RequestMapping("/api/admin/forbiddenWords")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ForbiddenController {
    final ForbiddenWordService forbiddenWordService;

    @GetMapping("/search")
    public ApiResponse<Page<ForbiddenWord>> getForbiddenWords(
            @RequestParam(defaultValue = "") String word,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ApiResponse.<Page<ForbiddenWord>>builder()
                .body(forbiddenWordService.getForbiddenWords(word, page))
                .build();
    }

    @PostMapping("/new")
    public ApiResponse<ForbiddenWord> createForbiddenWord(
            @RequestBody String word) {
        return ApiResponse.<ForbiddenWord>builder()
                .body(forbiddenWordService.create(word))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteForbiddenWord(@RequestParam String id) {
        forbiddenWordService.delete(id);
        return ApiResponse.<Void>builder().build();
    }
}
