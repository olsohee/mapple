package mapple.mapple.meeting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class CreateMeetingRequest {

    private String meetingName;
    private List<String> memberEmails;
}
