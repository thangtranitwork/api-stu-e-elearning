package thangtranit.com.elearning.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import thangtranit.com.elearning.dto.response.user.OtpVerifyResponse;
import thangtranit.com.elearning.entity.user.Otp;
import thangtranit.com.elearning.entity.user.Platform;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.repository.user.OtpRepository;
import thangtranit.com.elearning.repository.user.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpRepository otpRepository;

    @Value("${otp.duration}")
    private int OTP_DURATION;

    public OtpService(UserRepository userRepository, EmailService emailService, OtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.otpRepository = otpRepository;
    }

    public void sendOtp(String toEmail) {
        String otpStr = generateOTP();
        Date expiryTime = new Date(Instant.now().plus(OTP_DURATION, ChronoUnit.MINUTES).toEpochMilli());
        User user = userRepository.findByEmailAndPlatform(toEmail, Platform.APP).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        Otp otp = otpRepository.findByUserEmail(user.getEmail())
                .map(existingOtp -> {
                    existingOtp.setOtp(otpStr);
                    existingOtp.setExpiryDate(expiryTime);
                    return existingOtp;
                })
                .orElseGet(() ->  Otp.builder()
                        .otp(otpStr)
                        .expiryDate(expiryTime)
                        .user(user)
                        .build());

        otpRepository.save(otp);

        Map<String, Object> variables = new HashMap<>();

        variables.put("otp", otp.getOtp());

        String OTP_EMAIL_SUBJECT = "Your OTP Code";
        String OTP_EMAIL_TEMPLATE = "otp-email";
        emailService.sendMail(toEmail, OTP_EMAIL_SUBJECT, variables, OTP_EMAIL_TEMPLATE);
    }

    public OtpVerifyResponse verifyOtp(String email, String inputOtp) {
        Otp otp = otpRepository.findByUserEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.OTP_NOT_FOUND));
        if(otp.getRemaining() == 0){
            throw new AppException(ErrorCode.OTP_HAS_EXCEED_THE_NUMBER_OF_TRIES);
        }

        if (otp.getExpiryDate().after(new Date())) {
            if (otp.getOtp().equals(inputOtp)) {
                otp.setVerified(true);
                otpRepository.save(otp);
                return OtpVerifyResponse.builder()
                        .success(true)
                        .build();
            } else {
                otp.setRemaining(otp.getRemaining() - 1);
                otpRepository.save(otp);
                return OtpVerifyResponse.builder()
                        .success(false)
                        .remaining(otp.getRemaining())
                        .build();
            }
        } else {
            throw new AppException(ErrorCode.OTP_NOT_FOUND);
        }
    }


    public void useOtp(String email) {
        Otp otp = otpRepository.findByUserEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.OTP_NOT_FOUND));
        if(otp.getExpiryDate().before(new Date())){
            throw new AppException(ErrorCode.OTP_HAS_ALREADY_EXPIRED);
        }
        if (otp.isVerified()) {
            otp.getUser().setOtp(null);
            otpRepository.delete(otp);
        } else {
            throw new AppException(ErrorCode.OTP_NOT_VERIFIED);
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
