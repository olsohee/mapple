package mapple.mapple.user.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.CustomJwtException;
import mapple.mapple.user.entity.CustomUserDetails;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailManager implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new CustomJwtException(ErrorCodeAndMessage.INVALID_TOKEN));
        return CustomUserDetails.createFromEntity(user);
    }
}
