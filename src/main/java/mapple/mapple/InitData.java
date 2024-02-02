package mapple.mapple;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.repository.FriendRepository;
import mapple.mapple.review.entity.Rating;
import mapple.mapple.review.entity.Review;
import mapple.mapple.review.repository.ReviewRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final ReviewRepository reviewRepository;

    @PostConstruct
    public void initMember() {
        User user1 = User.create("kim", "kim@naver.com", "1234", "01012345678");
        userRepository.save(user1);

        User user2 = User.create("lee", "lee@naver.com", "1234", "01012345678");
        userRepository.save(user2);

        User user3 = User.create("park", "park@naver.com", "1234", "01012345678");
        userRepository.save(user3);
    }

    @PostConstruct
    public void initFriend() {
        User user1 = userRepository.findByIdentifier("kim@naver.com").get();
        User user2 = userRepository.findByIdentifier("lee@naver.com").get();

        Friend friend1 = Friend.create(user1, user2);
        friend1.acceptRequest();
        friendRepository.save(friend1);

        Friend friend2 = Friend.create(user2, user1);
        friend2.acceptRequest();
        friendRepository.save(friend2);
    }

    @PostConstruct
    public void initReview() {
        User user1 = userRepository.findByIdentifier("kim@naver.com").get();

        Review review1 = Review.create("미금역 팔각도", "맛있음!!", "https://map.naver.com/p/entry/place/1086631281?lng=127.1108268&lat=37.3507115&placePath=%2Fhome&entry=plt&searchType=place&c=15.00,0,0,0,dh",
                PublicStatus.PUBLIC, Rating.FOUR, user1);
        reviewRepository.save(review1);

        Review review2 = Review.create("오리역 팔각도", "미금이랑 똑같이 맛있는데 사람 없어서 추천", "https://map.naver.com/p/search/%EC%98%A4%EB%A6%AC%20%ED%8C%94%EA%B0%81%EB%8F%84/place/1889168674?c=15.00,0,0,0,dh&isCorrectAnswer=true",
                PublicStatus.ONLY_FRIEND, Rating.FIVE, user1);
        reviewRepository.save(review2);

        User user2 = userRepository.findByIdentifier("lee@naver.com").get();

        Review review3 = Review.create("판교 현백 번패티번", "수제버거 맛있음~~", null,
                PublicStatus.ONLY_FRIEND, Rating.FOUR, user2);
        reviewRepository.save(review3);

        Review review4 = Review.create("미금 이가네 양꼬치", "비위생적임. 가지마세요 다들 ㅜㅜ", "https://map.naver.com/p/search/%EB%AF%B8%EA%B8%88%20%EC%9D%B4%EA%B0%80%EB%84%A4/place/1022337336?c=15.00,0,0,0,dh&placePath=%3Fentry%253Dbmp",
                PublicStatus.PRIVATE, Rating.ONE, user2);
        reviewRepository.save(review4);

        Review review5 = Review.create("정자 닭갈비", "담에 또 가야지", null,
                PublicStatus.PUBLIC, Rating.ONE, user2);
        reviewRepository.save(review5);
    }
}
