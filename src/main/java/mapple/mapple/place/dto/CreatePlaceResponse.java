package mapple.mapple.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.review.entity.Rating;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaceResponse {

    private String meetingName;
    private String placeName;
    private String content;
    private String url;
    private PublicStatus publicStatus;
    private LocalDateTime createdAt;
}
