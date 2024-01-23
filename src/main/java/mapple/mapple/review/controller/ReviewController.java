package mapple.mapple.review.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.review.dto.CreateReviewRequest;
import mapple.mapple.review.dto.CreateReviewResponse;
import mapple.mapple.review.service.ReviewService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtils jwtUtils;

    @PostMapping("/review")
    public CreateReviewResponse createReview(@Validated @RequestPart CreateReviewRequest dto,
                                             @RequestPart(required = false) List<MultipartFile> files,
                                             HttpServletRequest request) throws IOException {
        String token = jwtUtils.getTokenFromHeader(request);
        String email = jwtUtils.getEmailFromToken(token);
        return reviewService.createReview(dto, files, email);
    }
}
