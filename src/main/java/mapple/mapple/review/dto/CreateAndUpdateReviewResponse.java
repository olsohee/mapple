package mapple.mapple.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.review.entity.Rating;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAndUpdateReviewResponse {

    private String placeName;
    private String content;
    private String url;
    private PublicStatus publicStatus;
    private Rating rating;
    private LocalDateTime createdAt;
}
