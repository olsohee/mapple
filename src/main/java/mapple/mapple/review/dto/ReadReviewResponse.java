package mapple.mapple.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.review.entity.Rating;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
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
}
