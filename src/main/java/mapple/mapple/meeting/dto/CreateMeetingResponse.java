package mapple.mapple.meeting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.meeting.entity.Meeting;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateMeetingResponse {

    private String meetingName;
    private List<String> userNames;

    public CreateMeetingResponse(Meeting meeting) {
        this.meetingName = meeting.getMeetingName();
        this.userNames = meeting.getUserMeetings().stream()
                .map(userMeeting -> userMeeting.getUser().getUsername())
                .toList();
    }
}
