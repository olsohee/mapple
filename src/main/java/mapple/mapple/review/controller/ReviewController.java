package mapple.mapple.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.SuccessResponse;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.dto.CreateAndUpdateReviewResponse;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.dto.ReadReviewResponse;
import mapple.mapple.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity create(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                 @RequestPart(required = false) List<MultipartFile> files,
                                 HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        CreateAndUpdateReviewResponse responseData = reviewService.create(dto, files, identifier);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("리뷰 생성 성공", responseData));
    }

    @GetMapping("/reviews")
    public ResponseEntity readReadableAllReviews(HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        List<ReadReviewListResponse> responseData = reviewService.readReadableAllReviews(identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("전체 리뷰 리스트 조회 성공", responseData));
    }

    @GetMapping("/reviews/friend")
    public ResponseEntity readFriendsReviews(HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        List<ReadReviewListResponse> responseData = reviewService.readFriendsReviews(identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("친구 리뷰 리스트 조회 성공", responseData));
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity readOne(@PathVariable("reviewId") long reviewId, HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        ReadReviewResponse responseData = reviewService.readOne(reviewId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("단일 리뷰 조회 성공", responseData));
    }

    @PutMapping("/review/{reviewId}")
    public ResponseEntity update(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                 @RequestPart(required = false) List<MultipartFile> files,
                                 @PathVariable("reviewId") long reviewId,
                                 HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        CreateAndUpdateReviewResponse responseData = reviewService.updateReview(reviewId, identifier, dto, files);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("리뷰 수정 성공", responseData));
    }

    @DeleteMapping("/review/{reviewId}")
    public void delete(@PathVariable("reviewId") long reviewId, HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        reviewService.delete(reviewId, identifier);
    }
}
