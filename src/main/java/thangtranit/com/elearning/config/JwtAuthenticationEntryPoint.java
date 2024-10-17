package thangtranit.com.elearning.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import thangtranit.com.elearning.dto.response.ApiResponse;
import thangtranit.com.elearning.exception.ErrorCode;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        ApiResponse.<String>builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build())
        );
        response.getWriter().flush();
    }
}
