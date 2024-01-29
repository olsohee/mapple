package mapple.mapple.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final JwtUtils jwtUtils;
    private final ReviewService reviewService;

    @GetMapping("/mypage/reviews")
    public List<ReadReviewListResponse> readAll(HttpServletRequest request) {
        String token = jwtUtils.getTokenFromHeader(request);
        String identifier = jwtUtils.getIdentifierFromToken(token);
        return reviewService.readAllByUserIdentifier(identifier);
    }
}
