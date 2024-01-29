package mapple.mapple.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.dto.CreateAndUpdateReviewResponse;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.service.ReviewService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtils jwtUtils;

    @PostMapping("/review")
    public CreateAndUpdateReviewResponse createReview(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                                      @RequestPart(required = false) List<MultipartFile> files,
                                                      HttpServletRequest request) throws IOException {
        String token = jwtUtils.getTokenFromHeader(request);
        String identifier = jwtUtils.getIdentifierFromToken(token);
        return reviewService.createReview(dto, files, identifier);
    }

    @GetMapping("/reviews")
    public List<ReadReviewListResponse> readAll() {
        return reviewService.readAll();
    }

    @PutMapping("/review/{reviewId}")
    public CreateAndUpdateReviewResponse updateReview(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                                      @RequestPart(required = false) List<MultipartFile> files,
                                                      @PathVariable("reviewId") long reviewId,
                                                      HttpServletRequest request) throws IOException {
        String token = jwtUtils.getTokenFromHeader(request);
        String identifier = jwtUtils.getIdentifierFromToken(token);
        return reviewService.updateReview(reviewId, identifier, dto, files);
    }
}
