package mapple.mapple.validator;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.BusinessException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.repository.FriendQueryRepository;
import mapple.mapple.review.entity.Review;
import mapple.mapple.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewValidator {

    private final FriendQueryRepository friendQueryRepository;

    public void validateReviewAuthorization(Review review, User user) {
        if (!review.getUser().equals(user)) {
            throw new UserException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }

    public void validateFriend(User reviewUser, User readUser) {
        List<Friend> friends = friendQueryRepository.findFriendsByUser(reviewUser);
        boolean isFriend = friends.stream()
                .anyMatch(friend -> friend.getToUser().equals(readUser));
        if (!isFriend) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }

    public void validateIsNotPrivateReview(Review review) {
        if (review.isPrivate()) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }
}
