package mapple.mapple.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAndUpdateReviewRequest {

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
