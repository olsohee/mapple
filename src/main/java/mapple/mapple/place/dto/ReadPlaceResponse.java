package mapple.mapple.place.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.place.entity.Place;
import mapple.mapple.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReadPlaceResponse {

    private String username;
    private String placeName;
    private String content;
    private String url;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<byte[]> images;

    public ReadPlaceResponse(User user, Place place, List<byte[]> images) {
        this.username = user.getUsername();
        this.placeName = place.getPlaceName();
        this.content = place.getContent();
        this.url = place.getUrl();
        this.createdDate = place.getCreatedAt();
        this.updatedDate = place.getUpdatedAt();
        this.images = images;
    }
}
