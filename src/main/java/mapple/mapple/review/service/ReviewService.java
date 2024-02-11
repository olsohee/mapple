package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.ReviewException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.entity.Rating;
import mapple.mapple.review.entity.Review;
import mapple.mapple.review.repository.ReviewRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import mapple.mapple.validator.ReviewValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewValidator reviewValidator;

    @Value("${file.dir.review_image}")
    private String reviewImageFileDir;

    public long create(CreateAndUpdateReviewRequest dto, List<MultipartFile> files, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Review review = Review.create(dto.getPlaceName(), dto.getContent(), dto.getUrl(),
                PublicStatus.find(dto.getPublicStatus()), Rating.find(dto.getRating()), user);

        if (files != null) {
            review.updateImages(files, reviewImageFileDir);
        }

        reviewRepository.save(review);
        return review.getId();
    }

    public long update(long reviewId, String identifier, CreateAndUpdateReviewRequest dto, List<MultipartFile> files) throws IOException {
        Review review = findReviewByIdWithUser(reviewId);
        User user = findUserByIdentifier(identifier);

        reviewValidator.validateReviewAuthorization(review, user);

        review.update(dto.getPlaceName(), dto.getContent(), Rating.find(dto.getRating()),
                PublicStatus.find(dto.getPublicStatus()), dto.getUrl());

        if (files == null) {
            review.deleteImages();
        } else {
            review.updateImages(files, reviewImageFileDir);
        }

        return reviewId;
    }

    public void delete(long reviewId, String identifier) {
        Review review = findReviewByIdWithUser(reviewId);
        User user = findUserByIdentifier(identifier);
        reviewValidator.validateReviewAuthorization(review, user);
        reviewRepository.delete(review);
    }

    public void like(long reviewId, String identifier) {
        Review review = reviewRepository.findReviewWithLock(reviewId);
        User user = findUserByIdentifier(identifier);
        if (review.isLikeUser(user)) {
            review.unlike(user);
        } else {
            review.like(user);
        }
    }

    private User findUserByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
    }

    private Review findReviewByIdWithUser(long reviewId) {
        return reviewRepository.findByIdWithUser(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCodeAndMessage.NOT_FOUND_REVIEW));
    }
}
