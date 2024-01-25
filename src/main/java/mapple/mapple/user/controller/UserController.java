package mapple.mapple.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapple.mapple.jwt.JwtDto;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.user.dto.JoinRequest;
import mapple.mapple.user.dto.JoinResponse;
import mapple.mapple.user.dto.LoginRequest;
import mapple.mapple.user.dto.OAuthJoinRequest;
import mapple.mapple.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/join")
    public JoinResponse join(@Validated @RequestBody JoinRequest dto) {
        return userService.join(dto);
    }

    @PostMapping("/join/oauth")
    public JwtDto joinOAuth(@Validated @RequestBody OAuthJoinRequest dto) {
        // 회원가입
        userService.joinOAuth(dto);
        // 로그인
        return jwtUtils.generateToken(dto.getEmail());
    }

    @PostMapping("/login")
    public JwtDto login(@Validated @RequestBody LoginRequest dto) {
        return userService.login(dto);
    }


    /**
     * access token 만료시, refresh token을 통해 토큰 갱신
     */
    @PostMapping("/token")
    public JwtDto renewToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getTokenFromHeader(request);
        String email = jwtUtils.validateRefreshToken(refreshToken);
        return jwtUtils.generateToken(email);
    }

    @GetMapping("/login/oauth")
    public JwtDto successOAuth(@RequestParam("access_token") String accessToken,
                               @RequestParam("refresh_token") String refreshToken) {
        return new JwtDto(accessToken, refreshToken);
    }
}
