package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.Image;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.entity.Rating;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.UserException;
import mapple.mapple.review.entity.Review;
import mapple.mapple.review.dto.CreateReviewRequest;
import mapple.mapple.review.dto.CreateReviewResponse;
import mapple.mapple.review.repository.ReviewRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Value("${file.dir.review_image}")
    private String reviewImageFileDir;

    public CreateReviewResponse createReview(CreateReviewRequest dto, List<MultipartFile> files, String email) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_EMAIL));

        Review review = Review.builder().placeName(dto.getPlaceName())
                .content(dto.getContent())
                .url(dto.getUrl())
                .publicStatus(PublicStatus.find(dto.getPublicStatus()))
                .rating(Rating.find(dto.getRating()))
                .user(user)
                .images(new ArrayList<>())
                .build();


        for (MultipartFile file : files) {
            String updatedName = file.getOriginalFilename();
            String storedName = getStoredName(updatedName);
            file.transferTo(new File(reviewImageFileDir + storedName));

            Image.create(storedName, updatedName, reviewImageFileDir, review);
        }

        reviewRepository.save(review);
        return new CreateReviewResponse(review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt());
    }

    private String getStoredName(String updatedName) {
        int pos = updatedName.lastIndexOf(".");
        String ext = updatedName.substring(pos + 1);
        return UUID.randomUUID() + "." + ext;
    }
}
