package mapple.mapple.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.review.entity.Rating;
import mapple.mapple.review.entity.Review;
import mapple.mapple.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReadReviewListResponse {

    private String username;
    private String placeName;
    private Rating rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReadReviewListResponse(User user, Review review) {
        this.username = user.getUsername();
        this.placeName = review.getPlaceName();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
