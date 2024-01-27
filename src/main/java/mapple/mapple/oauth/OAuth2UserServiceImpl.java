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
        String identifier = getIdentifier(oAuthProvider, oAuth2User);

        // 해당 identifier가 DB에 저장되어 있지 않은 식별자이면 회원가입
        Optional<User> optionalUser = userRepository.findByIdentifier(identifier);
        if (optionalUser.isEmpty()) {
            User user = User.createOAuthUser(identifier, oAuthProvider);
            userRepository.save(user);
        }

        return super.loadUser(userRequest);
    }

    private String getIdentifier(OAuthProvider oAuthProvider, OAuth2User oAuth2User) {
        if (oAuthProvider == OAuthProvider.KAKAO) {
            return oAuth2User.getName();

        }
        else if (oAuthProvider == OAuthProvider.NAVER) {
            String[] arr = oAuth2User.getName().split(", ");
            return arr[0].substring(4);
        }
        return null;
    }
}
