package mapple.mapple.review.entity;

import mapple.mapple.entity.Image;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewLikeTest {

    @Test
    @DisplayName("리뷰 좋아요 생성")
    void createReviewLike() {
        // given
        User reviewUser = User.create("reviewUser", "reviewUser@naver.com", "1234", "01012345678");
        Review review = Review.create("판교역 양꼬치 맛집 추천!", "추천추천!", "https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.PUBLIC, Rating.THREE, reviewUser);

        User likeUser = User.create("likeUser", "likeUser@naver.com", "1234", "01012345678");

        // when
        ReviewLike reviewLike = ReviewLike.create(review, likeUser);

        // then
        Assertions.assertThat(reviewLike.getReview().getPlaceName()).isEqualTo("판교역 양꼬치 맛집 추천!");
        Assertions.assertThat(reviewLike.getReview().getContent()).isEqualTo("추천추천!");
        Assertions.assertThat(reviewLike.getReview().getUrl()).isEqualTo("https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh");
        Assertions.assertThat(reviewLike.getReview().getPublicStatus()).isEqualTo(PublicStatus.PUBLIC);
        Assertions.assertThat(reviewLike.getReview().getRating()).isEqualTo(Rating.THREE);

        Assertions.assertThat(reviewLike.getUser().getUsername()).isEqualTo("likeUser");
        Assertions.assertThat(reviewLike.getUser().getIdentifier()).isEqualTo("likeUser@naver.com");
        Assertions.assertThat(reviewLike.getUser().getPassword()).isEqualTo("1234");
        Assertions.assertThat(reviewLike.getUser().getPhoneNumber()).isEqualTo("01012345678");
    }
}
