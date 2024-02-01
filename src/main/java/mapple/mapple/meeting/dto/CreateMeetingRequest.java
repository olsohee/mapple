package mapple.mapple.meeting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class CreateMeetingRequest {

    @NotBlank
    private String meetingName;

    private List<String> userEmails;
}
