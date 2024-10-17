package thangtranit.com.elearning.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomOAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    @Value("${FE_ORIGIN}")
    private String FE_ORIGIN;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        System.out.println("CustomOAuth2LoginFailureHandler onAuthenticationFailure");
        response.sendRedirect(FE_ORIGIN + "/oauth2/fail");
    }
}
