package mapple.mapple.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadPlaceListResponse {

    private String username;
    private String placeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
