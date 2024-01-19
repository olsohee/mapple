package mapple.mapple.review.controller;

import lombok.RequiredArgsConstructor;
import mapple.mapple.review.dto.CreateReviewRequest;
import mapple.mapple.review.dto.CreateReviewResponse;
import mapple.mapple.review.service.ReviewService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public CreateReviewResponse createReview(@Validated @RequestBody CreateReviewRequest dto) {
        return reviewService.createReview(dto);
    }
}
