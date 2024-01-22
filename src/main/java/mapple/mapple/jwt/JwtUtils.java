package mapple.mapple.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.CustomJwtException;
import mapple.mapple.user.entity.CustomUserDetails;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.service.UserDetailManager;
import mapple.mapple.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key signingKey;
    private final JwtParser jwtParser;
    private final UserDetailManager userDetailManager;

    public JwtUtils(@Value("${jwt.secret}") String secretKey, UserDetailManager userDetailManager) {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
        this.userDetailManager = userDetailManager;
    }

    /**
     * 최초 로그인시 JWT 토큰 생성 후 반환
     */
    public JwtDto generateToken(User user) {
        Claims claims = Jwts.claims()
                .setSubject(user.getEmail()); // jwt의 payload에 사용자 이메일 저장
        return new JwtDto(generateAccessToken(user, claims), generateRefreshToken(user, claims));
    }

    private String generateAccessToken(User user, Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 30))) // 만료: 30분 뒤
                .signWith(SignatureAlgorithm.HS256, signingKey) // 사용할 암호화 알고리즘과 secret 값
                .compact();
    }

    private String generateRefreshToken(User user, Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 60))) // 만료: 60분 뒤
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    /**
     * 헤더에서 JWT 토큰 가져오기
     */
    public String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.split(" ")[1];
        } else {
            throw new CustomJwtException(ErrorCode.NOT_INPUT_TOKEN);
        }
    }

    /**
     * AccessToken 유효성 검증
     */
    public boolean validateAccessToken(String token) {
        // jjwt 라이브러리는 jwt 해석시 유효하지 않은 jwt이면 예외를 발생시킨다
        try {
            jwtParser.parseClaimsJws(token); // jwt parse
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new CustomJwtException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Authentication generateAuthentication(String token) {
        // 토큰에 저장된 이메일 정보
        String email = jwtParser.parseClaimsJws(token).getBody().getSubject();
        CustomUserDetails user = (CustomUserDetails) userDetailManager.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        return authentication;
    }
}
