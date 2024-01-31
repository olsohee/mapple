package mapple.mapple.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMeetingResponse {

    private String meetingName;
    private List<String> userNames;
}
