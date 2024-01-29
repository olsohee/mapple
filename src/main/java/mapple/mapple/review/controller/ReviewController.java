package mapple.mapple.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.dto.CreateAndUpdateReviewResponse;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.dto.ReadReviewResponse;
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

    @GetMapping("/reviews")
    public List<ReadReviewListResponse> readAll() {
        return reviewService.readAll();
    }

    @GetMapping("/review/{reviewId}")
    public ReadReviewResponse read(@PathVariable("reviewId") long reviewId) {
        return reviewService.read(reviewId);
    }

    @PostMapping("/review")
    public CreateAndUpdateReviewResponse create(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                                @RequestPart(required = false) List<MultipartFile> files,
                                                HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        return reviewService.createReview(dto, files, identifier);
    }

    @PutMapping("/review/{reviewId}")
    public CreateAndUpdateReviewResponse update(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                                @RequestPart(required = false) List<MultipartFile> files,
                                                @PathVariable("reviewId") long reviewId,
                                                HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        return reviewService.updateReview(reviewId, identifier, dto, files);
    }

    @DeleteMapping("/review/{reviewId}")
    public void delete(@PathVariable("reviewId") long reviewId,
                       HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        reviewService.delete(reviewId, identifier);
    }
}
