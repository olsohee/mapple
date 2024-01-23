package mapple.mapple.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtDto;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.user.dto.JoinRequest;
import mapple.mapple.user.dto.JoinResponse;
import mapple.mapple.user.dto.LoginRequest;
import mapple.mapple.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/join")
    public JoinResponse join(@Validated @RequestBody JoinRequest dto) {
        return userService.join(dto);
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
        System.out.println("컨트롤러 호");
        String refreshToken = jwtUtils.getTokenFromHeader(request);
        String email = jwtUtils.validateRefreshToken(refreshToken);
        return jwtUtils.generateToken(email);
    }
}
