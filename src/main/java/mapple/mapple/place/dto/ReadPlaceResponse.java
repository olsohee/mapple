package mapple.mapple.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadPlaceResponse {

    private String username;
    private String placeName;
    private String content;
    private String url;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<byte[]> images;
}
