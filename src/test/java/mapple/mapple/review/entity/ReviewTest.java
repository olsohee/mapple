package mapple.mapple.review.entity;

import mapple.mapple.entity.PublicStatus;
import mapple.mapple.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewTest {

    User user;
    Review review;
    Review publicReview;
    Review onlyFriendReview;
    Review privateReview;

    @BeforeEach
    void init() {
        user = User.create("user", "user@naver.com", "1234", "01012345678");
        review = Review.create("판교역 양꼬치 맛집 추천!", "추천추천!", "https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.PUBLIC, Rating.THREE, user);
        publicReview = Review.create("판교역 양꼬치 맛집 추천!", "추천추천!", "https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.PUBLIC, Rating.THREE, user);
        onlyFriendReview = Review.create("판교역 양꼬치 맛집 추천!", "추천추천!", "https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.ONLY_FRIEND, Rating.THREE, user);
        privateReview = Review.create("판교역 양꼬치 맛집 추천!", "추천추천!", "https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.PRIVATE, Rating.THREE, user);
    }

    @Test
    @DisplayName("리뷰 생성")
    void createReview() {
        // then
        Assertions.assertThat(review.getPlaceName()).isEqualTo("판교역 양꼬치 맛집 추천!");
        Assertions.assertThat(review.getContent()).isEqualTo("추천추천!");
        Assertions.assertThat(review.getUrl()).isEqualTo("https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh");
        Assertions.assertThat(review.getPublicStatus()).isEqualTo(PublicStatus.PUBLIC);
        Assertions.assertThat(review.getRating()).isEqualTo(Rating.THREE);
    }

    @Test
    @DisplayName("리뷰 수정")
    void updateReview() {
        // when
        review.update("판교역 이가네 양꼬치", "추천추천!", "https://map.naver.com/p/entry/place/37362481?lng=127.1125236&lat=37.3976391&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.PUBLIC, Rating.THREE);

        // then
        Assertions.assertThat(review.getPlaceName()).isEqualTo("판교역 이가네 양꼬치");
    }

    @Test
    @DisplayName("리뷰 좋아요 등록")
    void likeReview() {
        // when
        review.likeOrUnlike(user);

        // then
        Assertions.assertThat(review.getLikeCount()).isEqualTo(1);
        Assertions.assertThat(review.getLikes().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 좋아요 취소")
    void unlikeReview() {
        // when
        review.likeOrUnlike(user);
        review.likeOrUnlike(user);

        // then
        Assertions.assertThat(review.getLikeCount()).isEqualTo(0);
        Assertions.assertThat(review.getLikes().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("리뷰 공개 범위: 공개")
    void isPublic() {
        // then
        Assertions.assertThat(publicReview.isPublic()).isTrue();
        Assertions.assertThat(onlyFriendReview.isPublic()).isFalse();
        Assertions.assertThat(privateReview.isPublic()).isFalse();
    }

    @Test
    @DisplayName("리뷰 공개 범위: 친구공개")
    void isOnlyFriend() {
        // then
        Assertions.assertThat(publicReview.isOnlyFriend()).isFalse();
        Assertions.assertThat(onlyFriendReview.isOnlyFriend()).isTrue();
        Assertions.assertThat(privateReview.isOnlyFriend()).isFalse();
    }

    @Test
    @DisplayName("리뷰 공개 범위: 비공개")
    void isPrivate() {
        // then
        Assertions.assertThat(publicReview.isPrivate()).isFalse();
        Assertions.assertThat(onlyFriendReview.isPrivate()).isFalse();
        Assertions.assertThat(privateReview.isPrivate()).isTrue();
    }
}
