package mapple.mapple.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapple.mapple.exception.customException.CustomJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter 실행");
        try {
            // 요청 헤더에 토큰 유무 검증
            String token = jwtUtils.getTokenFromHeader(request);

            // 토큰 유효성 검증
            if (jwtUtils.validateAccessToken(token)) {
                // 토큰이 유효하면 Authentication을 SecurityContextHolder에 저장
                Authentication authentication = jwtUtils.generateAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("인증 정보(authentication) 저장 = {}", authentication.getPrincipal());
            }
        } catch (CustomJwtException e) {
            log.error(e.toString());
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }
}
