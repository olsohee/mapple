package mapple.mapple.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.entity.Rating;

@Getter
@NoArgsConstructor
public class CreateReviewRequest {

    @NotBlank
    private String placeName;

    @NotBlank
    private String content;

    private String url;

    @NotBlank
    private String publicStatus;

    @NotBlank
    private String rating;
}
