package mapple.mapple.review.entity;

import mapple.mapple.entity.Image;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewImageTest {

    @Test
    @DisplayName("리뷰 이미지 생성")
    void createReviewImage() {
        // given
        User user = User.create("user", "user@naver.com", "1234", "01012345678");
        Review review = Review.create("판교역 양꼬치 맛집 추천!", "추천추천!", "https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.PUBLIC, Rating.THREE, user);
        Image image = Image.create("storedName", "updatedName", "/review_image");

        // when
        ReviewImage reviewImage = ReviewImage.create(image, review);

        // then
        Assertions.assertThat(reviewImage.getReview().getPlaceName()).isEqualTo("판교역 양꼬치 맛집 추천!");
        Assertions.assertThat(reviewImage.getReview().getContent()).isEqualTo("추천추천!");
        Assertions.assertThat(reviewImage.getReview().getUrl()).isEqualTo("https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh");
        Assertions.assertThat(reviewImage.getReview().getPublicStatus()).isEqualTo(PublicStatus.PUBLIC);
        Assertions.assertThat(reviewImage.getReview().getRating()).isEqualTo(Rating.THREE);

        Assertions.assertThat(reviewImage.getImage().getStoredName()).isEqualTo("storedName");
        Assertions.assertThat(reviewImage.getImage().getUpdatedName()).isEqualTo("updatedName");
        Assertions.assertThat(reviewImage.getImage().getStoreDir()).isEqualTo("/review_image");
    }
}
