package mapple.mapple.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.review.entity.Rating;
import mapple.mapple.review.entity.Review;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReadReviewListResponse {

    private String username;
    private String placeName;
    private Rating rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReadReviewListResponse(Review review) {
        this.username = review.getUser().getUsername();
        this.placeName = review.getPlaceName();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
