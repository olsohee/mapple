package mapple.mapple.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.CustomJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 요청 헤더에 토큰 유무 검증
            String token = jwtUtils.getTokenFromHeader(request);

            // 토큰 유효성 건증
            if (jwtUtils.validateAccessToken(token)) {
                // 토큰이 유효하면 Authentication을 SecurityContextHolder에 저장
                Authentication authentication = jwtUtils.generateAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (CustomJwtException e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }
}
