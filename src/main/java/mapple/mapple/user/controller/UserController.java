package mapple.mapple.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapple.mapple.jwt.JwtDto;
import mapple.mapple.user.dto.JoinRequest;
import mapple.mapple.user.dto.JoinResponse;
import mapple.mapple.user.dto.LoginRequest;
import mapple.mapple.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity join(@Validated @RequestBody JoinRequest dto) {
        JoinResponse responseData = userService.join(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody LoginRequest dto) {
        JwtDto responseData = userService.login(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseData);
    }

    @GetMapping("/login/oauth")
    public ResponseEntity oAuthLogin(@RequestParam("access_token") String accessToken,
                             @RequestParam("refresh_token") String refreshToken) {
        JwtDto responseData = new JwtDto(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseData);
    }

    /**
     * access token 만료시, refresh token을 통해 토큰 갱신
     */
    @PostMapping("/token")
    public ResponseEntity renewToken(HttpServletRequest request) {
        JwtDto responseData = userService.renewToken(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseData);
    }
}
