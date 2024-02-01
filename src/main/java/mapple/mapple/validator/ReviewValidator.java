package mapple.mapple.validator;

import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.review.entity.Review;
import mapple.mapple.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewValidator {

    public void validateReviewAuthorization(Review review, User user) {
        if (!review.getUser().equals(user)) {
            throw new UserException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }
}
