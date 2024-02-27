package mapple.mapple.user.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("사용자 생성")
    void createUser() {
        // given
        User user = User.create("user", "user@naver.com", "1234", "01012345678");

        // then
        Assertions.assertThat(user.getUsername()).isEqualTo("user");
        Assertions.assertThat(user.getIdentifier()).isEqualTo("user@naver.com");
        Assertions.assertThat(user.getPassword()).isEqualTo("1234");
        Assertions.assertThat(user.getPhoneNumber()).isEqualTo("01012345678");
        Assertions.assertThat(user.getOAuthProvider()).isEqualTo(OAuthProvider.NONE);
    }

    @Test
    @DisplayName("OAuth 사용자 생성")
    void createOAuthUser() {
        // given
        User user = User.createOAuthUser("user@naver.com", OAuthProvider.NAVER);

        // then
        Assertions.assertThat(user.getIdentifier()).isEqualTo("user@naver.com");
        Assertions.assertThat(user.getOAuthProvider()).isEqualTo(OAuthProvider.NAVER);
    }
}
