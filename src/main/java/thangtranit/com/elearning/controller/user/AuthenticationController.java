package thangtranit.com.elearning.controller.user;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import thangtranit.com.elearning.dto.request.user.*;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.dto.response.user.AuthenticationResponse;
import thangtranit.com.elearning.dto.response.user.IntrospectResponse;
import thangtranit.com.elearning.dto.response.user.OtpVerifyResponse;
import thangtranit.com.elearning.service.user.AuthenticationService;
import thangtranit.com.elearning.service.user.RegisterService;
import thangtranit.com.elearning.service.user.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {
    final RegisterService registerService;
    final AuthenticationService authenticationService;
    final UserService userService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request, HttpServletResponse response) {
        return ApiResponse.<AuthenticationResponse>builder()
                .body(authenticationService.authenticate(request, response))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        if(refreshToken.isEmpty()) {
            return null;
        }
        authenticationService.logout(refreshToken, response);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .body(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/register")
    ApiResponse<Void> addUser(@RequestBody @Valid RegisterRequest request) {
        registerService.register(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/register/verify")
    ApiResponse<AuthenticationResponse> verifyEmail(
            @RequestParam(defaultValue = "") String code,
            HttpServletResponse response) {
        return ApiResponse.<AuthenticationResponse>builder()
                .body(registerService.verify(code, response))
                .build();
    }

    @GetMapping("/register/resend")
    ApiResponse<Void> resendCode(
            @RequestParam String email){
        registerService.resend(email);
        return ApiResponse.<Void>builder().build();
    }
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(
            @CookieValue(value = "refreshToken") String refreshToken) {
        return ApiResponse.<AuthenticationResponse>builder()
                .body(authenticationService.refreshToken(refreshToken))
                .build();
    }

    @GetMapping("/forgot-password/{email}")
    ApiResponse<Void> prepareForForgotPassword(@PathVariable String email) {
        userService.prepare(email);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PutMapping("/forgot-password/{email}")
    ApiResponse<Void> forgotPassword(@RequestBody @Valid ChangePasswordRequest request, @PathVariable String email) {
        userService.changePassword(email, request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/forgot-password/{email}/verify-otp")
    ApiResponse<OtpVerifyResponse> verifyOtp(@RequestBody @Valid OtpVerifyRequest request, @PathVariable String email) {
        return ApiResponse.<OtpVerifyResponse>builder()
                .body(userService.verifyOtp(email, request))
                .build();
    }
}
