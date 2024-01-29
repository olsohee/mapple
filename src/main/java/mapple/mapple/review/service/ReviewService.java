package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ReviewException;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.dto.ReadReviewResponse;
import mapple.mapple.review.entity.Image;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.entity.Rating;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.UserException;
import mapple.mapple.review.entity.Review;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.dto.CreateAndUpdateReviewResponse;
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

    public CreateAndUpdateReviewResponse createReview(CreateAndUpdateReviewRequest dto, List<MultipartFile> files, String email) throws IOException {
        User user = userRepository.findByIdentifier(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_EMAIL));

        Review review = Review.create(dto.getPlaceName(), dto.getContent(), dto.getUrl(),
                PublicStatus.find(dto.getPublicStatus()), Rating.find(dto.getRating()), user);

        for (MultipartFile file : files) {
            String updatedName = file.getOriginalFilename();
            String storedName = getStoredName(updatedName);
            file.transferTo(new File(reviewImageFileDir + storedName));

            Image image = Image.create(storedName, updatedName, reviewImageFileDir);
            review.addImage(image);
        }

        reviewRepository.save(review);
        return new CreateAndUpdateReviewResponse(review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt());
    }

    private String getStoredName(String updatedName) {
        int pos = updatedName.lastIndexOf(".");
        String ext = updatedName.substring(pos + 1);
        return UUID.randomUUID() + "." + ext;
    }

    public List<ReadReviewListResponse> readAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getUpdatedAt(), review.getCreatedAt()))
                .toList();
    }

    public ReadReviewResponse read(long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCode.NOT_FOUND_REVIEW));
        return new ReadReviewResponse(review.getUser().getUsername(), review.getPlaceName(), review.getContent(),
                review.getUrl(), review.getRating(), review.getCreatedAt(), review.getUpdatedAt());
    }

    public List<ReadReviewListResponse> readAllByUserIdentifier(String identifier) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_EMAIL));
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getUpdatedAt(), review.getCreatedAt()))
                .toList();
    }

    public CreateAndUpdateReviewResponse updateReview(long reviewId, String identifier, CreateAndUpdateReviewRequest dto, List<MultipartFile> files) throws IOException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCode.NOT_FOUND_REVIEW));
        validateAuthorization(review, identifier);

        review.update(dto.getPlaceName(), dto.getContent(), Rating.find(dto.getRating()),
                PublicStatus.find(dto.getPublicStatus()), dto.getUrl());

        List<Image> images = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String updatedName = file.getOriginalFilename();
                String storedName = getStoredName(updatedName);
                file.transferTo(new File(reviewImageFileDir + storedName));

                Image image = Image.create(storedName, updatedName, reviewImageFileDir);
                images.add(image);
            }
        }
        review.updateImages(images);

        return new CreateAndUpdateReviewResponse(review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt());
    }

    public void delete(long reviewId, String identifier) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCode.NOT_FOUND_REVIEW));
        validateAuthorization(review, identifier);
        reviewRepository.delete(review);
    }

    private void validateAuthorization(Review review, String identifier) {
        if (!review.getUser().getIdentifier().equals(identifier)) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }
    }
}
