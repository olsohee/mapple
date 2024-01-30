package mapple.mapple.place.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePlaceRequest {

    @NotNull
    private long meetingId;

    @NotBlank
    private String placeName;

    @NotBlank
    private String content;

    private String url;

    @NotBlank
    private String publicStatus;
}
