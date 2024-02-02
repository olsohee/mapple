package mapple.mapple.validator;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.BusinessException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.review.entity.Review;
import mapple.mapple.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewValidator {

    public void validateReviewAuthorization(Review review, User user) {
        if (!review.getUser().equals(user)) {
            throw new UserException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }

    public void validateCanRead(Review review, User user, List<Friend> readUsersFriends) {
        // 비공개 -> 자기 리뷰가 아니면 접근 불가
        if (review.isPrivate() && !review.getUser().equals(user)) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        }

        // 친구공개 -> 자기글 또는 친구 글이 아니면 접근 불가
        if (review.isOnlyFriend() && !review.getUser().equals(user) && !isFriend(readUsersFriends, review.getUser())) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }

    private boolean isFriend(List<Friend> readUsersFriends, User reviewUser) {
        return readUsersFriends.stream()
                .anyMatch(friend -> friend.getToUser().equals(reviewUser));
    }
}
