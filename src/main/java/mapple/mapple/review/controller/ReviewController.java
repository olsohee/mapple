package mapple.mapple.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.SuccessResponse;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.dto.CreateAndUpdateReviewResponse;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.dto.ReadReviewResponse;
import mapple.mapple.review.service.ReviewQueryService;
import mapple.mapple.review.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    private final ReviewQueryService reviewQueryService;
    private final ReviewService reviewService;
    private final JwtUtils jwtUtils;

    @PostMapping("/review")
    public ResponseEntity create(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                 @RequestPart(required = false) List<MultipartFile> files,
                                 HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        long savedReviewId = reviewService.create(dto, files, identifier);
        CreateAndUpdateReviewResponse responseData = reviewQueryService.readCreatedUpdatedReview(savedReviewId, identifier);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("리뷰 생성 성공", responseData));
    }

    @PutMapping("/review/{reviewId}")
    public ResponseEntity update(@Validated @RequestPart CreateAndUpdateReviewRequest dto,
                                 @RequestPart(required = false) List<MultipartFile> files,
                                 @PathVariable("reviewId") long reviewId,
                                 HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        reviewService.update(reviewId, identifier, dto, files);
        CreateAndUpdateReviewResponse responseData = reviewQueryService.readCreatedUpdatedReview(reviewId, identifier);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("리뷰 수정 성공", responseData));
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity delete(@PathVariable("reviewId") long reviewId, HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        reviewService.delete(reviewId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("리뷰 삭제 성공"));

    }

    @GetMapping("/reviews")
    public ResponseEntity readReviews(@PageableDefault(size = 5, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                              @RequestParam(value = "keyword", required = false) String keyword,
                                              HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        Page<ReadReviewListResponse> responseData = reviewQueryService.readReviews(identifier, keyword, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("전체 리뷰 리스트 조회 성공", responseData));
    }

    @GetMapping("/reviews/friend")
    public ResponseEntity readFriendsReviews(@PageableDefault(size = 5, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                             HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        Page<ReadReviewListResponse> responseData = reviewQueryService.readFriendsReviews(identifier, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("친구 리뷰 리스트 조회 성공", responseData));
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity readOne(@PathVariable("reviewId") long reviewId, HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        ReadReviewResponse responseData = reviewQueryService.readOne(reviewId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("단일 리뷰 조회 성공", responseData));
    }

    @PostMapping("/review/{reviewId}/like")
    public ResponseEntity likeReview(@PathVariable("reviewId") long reviewId, HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        reviewService.like(reviewId, identifier);

        ReadReviewResponse responseData = reviewQueryService.readOne(reviewId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("좋아요 누르기/취소 성공", responseData));
    }

    @GetMapping("/reviews/like")
    public ResponseEntity readLikeReviews(@PageableDefault(size = 5, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                          HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        List<ReadReviewListResponse> responseData = reviewQueryService.readLikeReviews(identifier, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("좋아요 누른 리뷰 리스트 조회 성공", responseData));
    }
}
