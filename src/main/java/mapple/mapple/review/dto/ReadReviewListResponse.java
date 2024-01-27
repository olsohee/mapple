package mapple.mapple.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.Rating;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadReviewListResponse {

    private String username;
    private String placeName;
    private Rating rating;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}