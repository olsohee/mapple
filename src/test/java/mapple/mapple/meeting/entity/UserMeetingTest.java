package mapple.mapple.meeting.entity;

import mapple.mapple.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserMeetingTest {

    @Test
    @DisplayName("UserMeeting 생성")
    void createUserMeeting() {
        // given
        User user = User.create("user", "user@naver.com", "1234", "01012345678");
        Meeting meeting = Meeting.create("모임명");

        // when
        UserMeeting userMeeting = UserMeeting.create(user, meeting);

        // then
        Assertions.assertThat(userMeeting.getUser()).isEqualTo(user);
        Assertions.assertThat(userMeeting.getMeeting()).isEqualTo(meeting);
    }
}
