package mapple.mapple.validator;

import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.BusinessException;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MeetingValidator {

    public void validateMeetingMember(Meeting meeting, User user) {
        boolean hasAuthority = meeting.getUserMeetings().stream()
                .anyMatch(userMeeting -> userMeeting.getUser() == user);
        if (!hasAuthority) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }
}
