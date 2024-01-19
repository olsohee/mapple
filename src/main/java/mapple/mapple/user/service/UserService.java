package mapple.mapple.user.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.User;
import mapple.mapple.exception.BusinessException;
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

    private final UserRepository userRepository;

    public JoinResponse join(JoinRequest dto) {
        validateDuplication(dto.getEmail());
        User user = User.builder().email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .phoneNumber(dto.getPhoneNumber()).build();
        userRepository.save(user);

        return new JoinResponse(user.getEmail(), user.getCreatedAt());
    }

    private void validateDuplication(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(ErrorCode.DUPLICATED_EMAIL);
        }
    }
}
