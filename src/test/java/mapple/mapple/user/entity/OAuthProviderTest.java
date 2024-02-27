package mapple.mapple.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OAuthProviderTest {

    @Test
    @DisplayName("이름으로 OAuthProvider 찾기")
    void findByName() {
        // then
        assertThat(OAuthProvider.find("none")).isEqualTo(OAuthProvider.NONE);
        assertThat(OAuthProvider.find("kakao")).isEqualTo(OAuthProvider.KAKAO);
        assertThat(OAuthProvider.find("naver")).isEqualTo(OAuthProvider.NAVER);
    }
}
