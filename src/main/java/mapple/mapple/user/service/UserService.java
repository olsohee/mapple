package mapple.mapple.user.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.CustomJwtException;
import mapple.mapple.jwt.JwtDto;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.redis.RefreshToken;
import mapple.mapple.redis.RefreshTokenRepository;
import mapple.mapple.user.dto.LoginRequest;
import mapple.mapple.user.entity.User;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.user.dto.JoinRequest;
import mapple.mapple.user.dto.JoinResponse;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JoinResponse join(JoinRequest dto) {
        validateDuplication(dto.getEmail());
        User user = User.create(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getPhoneNumber());
        userRepository.save(user);
        return new JoinResponse(user.getIdentifier(), user.getCreatedAt());
    }

    private void validateDuplication(String email) {
        if (userRepository.findByIdentifier(email).isPresent()) {
            throw new UserException(ErrorCodeAndMessage.DUPLICATED_EMAIL);
        }
    }

    public JwtDto login(LoginRequest dto) {
        User user = validateEmail(dto.getEmail());
        validatePassword(user, dto.getPassword());
        JwtDto jwtDto = jwtUtils.generateToken(dto.getEmail());
        // refresh token redis 저장
        String refreshToken = jwtDto.getRefreshToken();
        refreshTokenRepository.save(new RefreshToken(user.getIdentifier(), refreshToken));
        return jwtDto;
    }

    private User validateEmail(String email) {
        return userRepository.findByIdentifier(email)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
    }

    private void validatePassword(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new UserException(ErrorCodeAndMessage.INVALID_PASSWORD);
        }
    }

    public JwtDto renewToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getTokenFromHeader(request);
        // refresh token 유효성 검사
        String identifier = jwtUtils.validateRefreshToken(refreshToken);
        // redis의 refresh token과 동일한지 검사
        RefreshToken savedToken = refreshTokenRepository.findById(identifier)
                .orElseThrow(() -> new CustomJwtException(ErrorCodeAndMessage.INVALID_TOKEN));
        if (!savedToken.getRefreshToken().equals(refreshToken)) {
            throw new CustomJwtException(ErrorCodeAndMessage.EXPIRED_TOKEN);
        }
        // 기존에 redis에 저장된 토큰 삭제 후 재발급 후 저장
        JwtDto jwtDto = jwtUtils.generateToken(identifier);
        savedToken.setRefreshToken(jwtDto.getRefreshToken());
        refreshTokenRepository.save(savedToken);
        return jwtDto;
    }
}
