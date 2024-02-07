package mapple.mapple.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.review.entity.Rating;
import mapple.mapple.review.entity.Review;
import mapple.mapple.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateAndUpdateReviewResponse {

    private String username;
    private String placeName;
    private String content;
    private String url;
    private PublicStatus publicStatus;
    private Rating rating;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<byte[]> images;

    public CreateAndUpdateReviewResponse(User user, Review review, Long likeCount, List<byte[]> images) {
        this.username = user.getUsername();
        this.placeName = review.getPlaceName();
        this.content = review.getContent();
        this.url = review.getUrl();
        this.publicStatus = review.getPublicStatus();
        this.rating = review.getRating();
        this.likeCount = likeCount;
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
        this.images = images;
    }
}
