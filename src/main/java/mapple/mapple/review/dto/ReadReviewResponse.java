package mapple.mapple.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.review.entity.Rating;
import mapple.mapple.review.entity.Review;
import mapple.mapple.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ReadReviewResponse {

    private String username;
    private String placeName;
    private String content;
    private String url;
    private Rating rating;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<byte[]> images;

    public ReadReviewResponse(User user, Review review, List<byte[]> images) {
        this.username = user.getUsername();
        this.placeName = review.getPlaceName();
        this.content = review.getContent();
        this.url = review.getUrl();
        this.rating = review.getRating();
        this.createdDate = review.getCreatedAt();
        this.updatedDate = review.getUpdatedAt();
        this.images = images;
    }
}
