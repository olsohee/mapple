package mapple.mapple.place.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAndUpdatePlaceRequest {

    @NotBlank
    private String placeName;

    @NotBlank
    private String content;

    private String url;
}
