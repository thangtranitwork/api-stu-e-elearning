package thangtranit.com.elearning.controller.user;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import thangtranit.com.elearning.dto.request.user.ChangeEmailRequest;
import thangtranit.com.elearning.dto.request.user.ChangePasswordRequest;
import thangtranit.com.elearning.dto.request.user.OtpVerifyRequest;
import thangtranit.com.elearning.dto.request.user.UserProfileRequest;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.user.ChangeEmailResponse;
import thangtranit.com.elearning.dto.response.user.OtpVerifyResponse;
import thangtranit.com.elearning.dto.response.user.UserMinimumInfoResponse;
import thangtranit.com.elearning.dto.response.user.UserProfileResponse;
import thangtranit.com.elearning.service.user.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserService userService;

    @GetMapping("/{id}")
    ApiResponse<UserProfileResponse> getUserProfile(@PathVariable String id) {
        return ApiResponse.<UserProfileResponse>builder()
                .body(userService.getProfile(id))
                .build();
    }

    @GetMapping("/profile")
    ApiResponse<UserProfileResponse> getProfile(){
        return ApiResponse.<UserProfileResponse>builder()
                .body(userService.getProfile())
                .build();
    }

    @GetMapping("/search")
    ApiResponse<Page<UserMinimumInfoResponse>> findUser(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ApiResponse.<Page<UserMinimumInfoResponse>>builder()
                .body(userService.findUsersByName(name, page))
                .build();
    }

    @GetMapping({"/update/email", "/update/password"})
    ApiResponse<Void> prepareForChangeLoginInfo() {
        userService.prepare();
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping({"/update/email/verify-otp", "/update/password/verify-otp"})
    ApiResponse<OtpVerifyResponse> verifyOtp(@RequestBody @Valid OtpVerifyRequest request) {
        return ApiResponse.<OtpVerifyResponse>builder()
                .body(userService.verifyOtp(request))
                .build();
    }

    @PutMapping("/update/email")
    ApiResponse<ChangeEmailResponse> changeUserEmail(@RequestBody @Valid ChangeEmailRequest request) {
        return ApiResponse.<ChangeEmailResponse>builder()
                .body(userService.changeEmail(request))
                .build();
    }

    @PutMapping("/update/password")
    ApiResponse<Void> changeUserPassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PutMapping("/update/avatar")
    ApiResponse<String> changeAvatar(@RequestParam MultipartFile avatar) {
        return ApiResponse.<String>builder()
                .body(userService.changeAvatar(avatar))
                .build();
    }

    @PutMapping("/update/profile")
    ApiResponse<UserProfileResponse> updateProfile(@RequestBody @Valid UserProfileRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .body(userService.updateProfile(request))
                .build();
    }

    @DeleteMapping("/delete")
    ApiResponse<Void> deleteUser() {
        userService.delete();
        return ApiResponse.<Void>builder()
                .build();
    }
}
