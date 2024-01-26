package mapple.mapple.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapple.mapple.user.entity.OAuthProvider;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2UserServiceImpl 실행");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthProvider oAuthProvider = OAuthProvider.find(registrationId);

        log.info("oAuth2User = {}", oAuth2User);
        String identifier="";
        if (oAuthProvider == OAuthProvider.KAKAO) {
            identifier = oAuth2User.getName();

        } else if (oAuthProvider == OAuthProvider.NAVER) {
            String[] arr = oAuth2User.getName().split(", ");
            identifier = arr[0].substring(4);
        }
        log.info("identifier = {}", identifier);

        // DB에 없으면 회원가입
        Optional<User> optionalUser = userRepository.findByIdentifier(identifier);
        if (optionalUser.isEmpty()) {
            User user = User.createOAuthUser(identifier, oAuthProvider);
            userRepository.save(user);
        }

        return super.loadUser(userRequest);
    }
}
