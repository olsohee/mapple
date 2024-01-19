package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.entity.Rating;
import mapple.mapple.entity.Review;
import mapple.mapple.review.dto.CreateReviewRequest;
import mapple.mapple.review.dto.CreateReviewResponse;
import mapple.mapple.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public CreateReviewResponse createReview(CreateReviewRequest dto) {
        Review review = Review.builder().placeName(dto.getPlaceName())
                .content(dto.getContent())
                .url(dto.getUrl())
                .publicStatus(PublicStatus.find(dto.getPublicStatus()))
                .rating(Rating.find(dto.getRating()))
                .build();
        reviewRepository.save(review);
        return new CreateReviewResponse(review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt());
    }
}
