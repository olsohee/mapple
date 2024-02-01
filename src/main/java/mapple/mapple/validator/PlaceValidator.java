package mapple.mapple.validator;

import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.BusinessException;
import mapple.mapple.place.entity.Place;
import mapple.mapple.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PlaceValidator {

    public void validatePlaceAuthorization(User user, Place place) {
        if (!place.getUser().equals(user)) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }
}
