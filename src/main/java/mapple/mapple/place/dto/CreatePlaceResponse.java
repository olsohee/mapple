package mapple.mapple.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaceResponse {

    private String username;
    private String meetingName;
    private String placeName;
    private String content;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<byte[]> images;
}
