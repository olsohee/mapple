package mapple.mapple.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.user.service.UserDetailManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 사용자 인증 완료 후, OAuth 회원가입 폼으로 리다이렉트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserDetailManager userDetailManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2SuccessHandler 실행");

        String redirectUrl = "http://localhost:8080/join/oauth"; // OAuth 회원가입 폼으로 이동
        DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
        defaultRedirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
