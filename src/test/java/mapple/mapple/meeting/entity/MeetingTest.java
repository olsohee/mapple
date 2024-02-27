package mapple.mapple.meeting.entity;

import mapple.mapple.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeetingTest {

    @Test
    @DisplayName("Meeting 생성")
    void createUserMeeting() {
        // given
        Meeting meeting = Meeting.create("모임명");
        User user1 = User.create("user1", "user@naver.com", "1234", "01012345678");
        User user2 = User.create("user2", "user@naver.com", "1234", "01012345678");

        // when
        UserMeeting userMeeting1 = UserMeeting.create(user1, meeting);
        UserMeeting userMeeting2 = UserMeeting.create(user2, meeting);
        meeting.addUserMeeting(userMeeting1);
        meeting.addUserMeeting(userMeeting2);

        // then
        Assertions.assertThat(meeting.getMeetingName()).isEqualTo("모임명");
        Assertions.assertThat(meeting.getUserMeetings().size()).isEqualTo(2);
        Assertions.assertThat(meeting.getUserMeetings().contains(userMeeting1)).isTrue();
        Assertions.assertThat(meeting.getUserMeetings().contains(userMeeting2)).isTrue();
    }
}
