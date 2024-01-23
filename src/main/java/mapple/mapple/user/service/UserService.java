package mapple.mapple.user.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtDto;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.user.dto.LoginRequest;
import mapple.mapple.user.entity.User;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.UserException;
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

    public JoinResponse join(JoinRequest dto) {
        validateDuplication(dto.getEmail());
        User user = User.create(dto.getEmail(), dto.getPassword(), dto.getUsername(), dto.getPhoneNumber());
        userRepository.save(user);
        return new JoinResponse(user.getEmail(), user.getCreatedAt());
    }

    private void validateDuplication(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    public JwtDto login(LoginRequest dto) {
        User user = validateEmail(dto.getEmail());
        validatePassword(user, dto.getPassword());
        return jwtUtils.generateToken(user.getEmail());
    }

    private User validateEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_EMAIL));
    }

    private void validatePassword(User user, String password) {
        if (!user.getPassword().equals(password)) {
            throw new UserException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
