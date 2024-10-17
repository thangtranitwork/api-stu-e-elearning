package thangtranit.com.elearning.service.user;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.request.user.OAuth2RegisterRequest;
import thangtranit.com.elearning.dto.request.user.RegisterRequest;
import thangtranit.com.elearning.dto.response.user.AuthenticationResponse;
import thangtranit.com.elearning.entity.user.Platform;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.entity.user.VerifyEmailCode;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.mapper.UserMapper;
import thangtranit.com.elearning.repository.user.UserRepository;
import thangtranit.com.elearning.repository.user.VerifyEmailCodeRepository;
import thangtranit.com.elearning.service.util.EmailService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterService {
    final UserRepository userRepository;
    final VerifyEmailCodeRepository verifyEmailCodeRepository;
    final PasswordEncoder passwordEncoder;
    final UserMapper userMapper;
    final EmailService emailService;
    final AuthenticationService authenticationService;
    @Value("${verify.email.duration}")
    int verifyEmailDuration;
    @Value("${FE_ORIGIN}")
    private String feOrigin;

    static final String VERIFY_EMAIL_TEMPLATE = "verify-email";
    static final String VERIFY_EMAIL_SUBJECT = "Xác minh email";

    public void register(RegisterRequest request) {
        validateUserDoesNotExist(request.getEmail(), request.getPlatform());

        if (Platform.APP.name().equals(request.getPlatform())) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User user = userMapper.toUser(request);
        userRepository.save(user);

        VerifyEmailCode verifyEmailCode = createAndSaveVerifyEmailCode(user);
        sendVerificationEmail(user.getEmail(), verifyEmailCode);
    }

    public void register(OAuth2RegisterRequest request) {
        validateUserDoesNotExist(request.getEmail(), request.getPlatform());
        User user = userMapper.toUser(request);
        userRepository.save(user);
    }

    private void validateUserDoesNotExist(String email, String platform) {
        userRepository.findByEmailAndPlatform(email, Platform.valueOf(platform)).ifPresent(u -> {
            if (u.isVerified()) {
                throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
            } else {
                throw new AppException(ErrorCode.USER_HAS_NOT_VERIFIED_EMAIL);
            }
        });

    }

    public VerifyEmailCode createAndSaveVerifyEmailCode(User user) {
        Date expiryTime = Date.from(Instant.now().plus(verifyEmailDuration, ChronoUnit.MINUTES));

        VerifyEmailCode verifyEmailCode = VerifyEmailCode.builder()
                .code(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(expiryTime)
                .build();

        verifyEmailCodeRepository.save(verifyEmailCode);
        return verifyEmailCode;
    }

    public void sendVerificationEmail(String email, VerifyEmailCode verifyEmailCode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("code", verifyEmailCode.getCode());
        variables.put("expiryDate", verifyEmailCode.getExpiryDate().toInstant().toString());
        variables.put("feOrigin", feOrigin);

        emailService.sendMail(email, VERIFY_EMAIL_SUBJECT, variables, VERIFY_EMAIL_TEMPLATE);
    }

    public AuthenticationResponse verify(String code, HttpServletResponse response) {
        try {
            UUID.fromString(code);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VERIFY_CODE_INVALID);
        }

        VerifyEmailCode verifyEmailCode = verifyEmailCodeRepository.findById(code)
                .orElseThrow(()->
                        new AppException(ErrorCode.VERIFY_CODE_DOES_NOT_EXIST));

        if (verifyEmailCode.getExpiryDate().before(new Date())){
            throw new AppException(ErrorCode.VERIFY_CODE_TIMEOUT);
        }

        User user = verifyEmailCode.getUser();
        user.setVerified(true);
        user.setVerifyEmailCode(null);
        authenticationService.updateOnline(user);
        verifyEmailCodeRepository.delete(verifyEmailCode);
        return authenticationService.generateResponse(user, response);
    }

    public void resend(String email) {
        VerifyEmailCode verifyEmailCode = verifyEmailCodeRepository.findByUserEmail(email)
                .orElseThrow(()->
                        new AppException(ErrorCode.VERIFY_CODE_DOES_NOT_EXIST));
        Date expiryTime = Date.from(Instant.now().plus(verifyEmailDuration, ChronoUnit.MINUTES));
        verifyEmailCode.setExpiryDate(expiryTime);
        verifyEmailCodeRepository.save(verifyEmailCode);
        sendVerificationEmail(email, verifyEmailCode);
    }
}
