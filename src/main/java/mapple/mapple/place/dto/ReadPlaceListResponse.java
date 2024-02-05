package mapple.mapple.place.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.place.entity.Place;
import mapple.mapple.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReadPlaceListResponse {

    private String username;
    private String placeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReadPlaceListResponse(User user, Place place) {
        this.username = user.getUsername();
        this.placeName = place.getPlaceName();
        this.createdAt = place.getCreatedAt();
        this.updatedAt = place.getUpdatedAt();
    }
}
