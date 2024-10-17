package thangtranit.com.elearning.controller.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.user.UserAdminViewResponse;
import thangtranit.com.elearning.service.user.UserService;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserAdminController {
    final UserService userService;

    @GetMapping
    public ApiResponse<Page<UserAdminViewResponse>> getAllUser(
            @RequestParam(defaultValue = "0") int page
    ) {
        return ApiResponse.<Page<UserAdminViewResponse>>builder()
                .body(userService.getAllUser(page))
                .build();
    }

    @PatchMapping("/grant")
    public ApiResponse<UserAdminViewResponse> grantAuthority(
            @RequestParam String id,
            @RequestParam String role
    ) {
        return ApiResponse.<UserAdminViewResponse>builder()
                .body(userService.grantAuthority(id, role))
                .build();
    }

    @PatchMapping("/revoke")
    public ApiResponse<UserAdminViewResponse> revokeAuthority(
            @RequestParam String id,
            @RequestParam String role
    ) {
        return ApiResponse.<UserAdminViewResponse>builder()
                .body(userService.revokeAuthority(id, role))
                .build();
    }
}
