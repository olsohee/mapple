package mapple.mapple;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final UserRepository userRepository;

    @PostConstruct
    public void initMember() {
        User user1 = User.create("kim", "kim@naver.com", "1234", "01012345678");
        userRepository.save(user1);

        User user2 = User.create("lee", "lee@naver.com", "1234", "01012345678");
        userRepository.save(user2);

        User user3 = User.create("park", "park@naver.com", "1234", "01012345678");
        userRepository.save(user3);
    }
}
