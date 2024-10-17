package thangtranit.com.elearning.service.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import thangtranit.com.elearning.entity.user.LoggedOutToken;
import thangtranit.com.elearning.entity.user.User;
import thangtranit.com.elearning.exception.AppException;
import thangtranit.com.elearning.exception.ErrorCode;
import thangtranit.com.elearning.repository.user.LoggedOutTokenRepository;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String secretKey;

    @Value("${jwt.accessToken.duration}")
    private int accessTokenDuration;

    @Value("${jwt.refreshToken.duration}")
    private int refreshTokenDuration;

    private final LoggedOutTokenRepository loggedOutTokenRepository;

    public String generateToken(User user, boolean isRefreshToken, String jit) {
        return createJwtToken(user, isRefreshToken, jit);
    }

    public String generateToken(User user, boolean isRefreshToken) {
        return generateToken(user, isRefreshToken, UUID.randomUUID().toString());
    }

    private String createJwtToken(User user, boolean isRefreshToken, String jit) {
        try {
            JWTClaimsSet jwtClaimsSet = buildJwtClaimsSet(user, isRefreshToken, jit);
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), new Payload(jwtClaimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error signing the token", e);
        }
    }

    private JWTClaimsSet buildJwtClaimsSet(User user, boolean isRefreshToken, String jit) {
        int duration = !isRefreshToken ? accessTokenDuration : refreshTokenDuration;
        ChronoUnit unit = !isRefreshToken ? ChronoUnit.MINUTES : ChronoUnit.DAYS;
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer("stu-e-learning")
                .subject(String.valueOf(user.getId()))
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(duration, unit)))
                .jwtID(jit)
                .claim("scope", user.getRoles())
                .claim("type", isRefreshToken ? "refresh-token" : "access-token");

        return builder.build();
    }

    public SignedJWT verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (isTokenExpiredOrInvalid(signedJWT) || isTokenLoggedOut(signedJWT)) {
                throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
            }
            return signedJWT;
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }
    }

    private boolean isTokenExpiredOrInvalid(SignedJWT signedJWT) {
        try {
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            Date expiryDate = signedJWT.getJWTClaimsSet().getExpirationTime();
            return !(signedJWT.verify(verifier) && expiryDate.after(new Date()));
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }
    }

    private boolean isTokenLoggedOut(SignedJWT signedJWT) throws java.text.ParseException {
        return loggedOutTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
    }

    public String getSub(String token) {
        SignedJWT signedJWT = verifyToken(token);
        try {
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }
    }


    public String getJit(String token) {
        SignedJWT signedJWT = verifyToken(token);
        try {
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }
    }

    public String getCurrentUserId() {
        return getJwt().getSubject();
    }


    private Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        } else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public boolean isAnonymousUser() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS"));
    }

    public boolean isRefreshToken(String token) {
        try {
            SignedJWT signedJWT = verifyToken(token);
            return signedJWT.getJWTClaimsSet().getClaim("type").equals("refresh-token");
        } catch (ParseException e) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }
    }

    public void logoutToken(String token) throws ParseException {
        SignedJWT signedJWT = verifyToken(token);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        LoggedOutToken loggedOutToken = LoggedOutToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        loggedOutTokenRepository.save(loggedOutToken);
    }
}
