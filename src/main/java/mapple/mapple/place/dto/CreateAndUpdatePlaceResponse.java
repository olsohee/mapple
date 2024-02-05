package mapple.mapple.place.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.place.entity.Place;
import mapple.mapple.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateAndUpdatePlaceResponse {

    private String username;
    private String meetingName;
    private String placeName;
    private String content;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<byte[]> images;

    public CreateAndUpdatePlaceResponse(User user, Meeting meeting, Place place, List<byte[]> images) {
        this.username = user.getUsername();
        this.meetingName = meeting.getMeetingName();
        this.placeName = place.getPlaceName();
        this.content = place.getContent();
        this.url = place.getUrl();
        this.createdAt = place.getCreatedAt();
        this.updatedAt = place.getUpdatedAt();
        this.images = images;
    }
}
