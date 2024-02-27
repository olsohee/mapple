package mapple.mapple.review.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RatingTest {

    @Test
    @DisplayName("평점 이름으로 Rating 찾기")
    void findByName() {
        // then
        assertThat(Rating.find("1")).isEqualTo(Rating.ONE);
        assertThat(Rating.find("2")).isEqualTo(Rating.TWO);
        assertThat(Rating.find("3")).isEqualTo(Rating.THREE);
        assertThat(Rating.find("4")).isEqualTo(Rating.FOUR);
        assertThat(Rating.find("5")).isEqualTo(Rating.FIVE);
    }
}
