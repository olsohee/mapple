package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.ReviewException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.redis.RedisCacheManager;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.dto.ReadReviewListResponse;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewValidator reviewValidator;
    private final RedisCacheManager redisCacheManager;

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

    public long update(long reviewId, String identifier, CreateAndUpdateReviewRequest dto, List<MultipartFile> files) throws IOException, ClassNotFoundException {
        checkBestReviewsCache(reviewId);

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

    public void delete(long reviewId, String identifier) throws IOException, ClassNotFoundException {
        checkBestReviewsCache(reviewId);
        Review review = findReviewByIdWithUser(reviewId);
        User user = findUserByIdentifier(identifier);
        reviewValidator.validateReviewAuthorization(review, user);
        reviewRepository.delete(review);
    }

    private void checkBestReviewsCache(long reviewId) throws IOException, ClassNotFoundException {
        int hour = LocalDateTime.now().getHour();
        byte[] savedDataInCache = redisCacheManager.getBestReviewsFromCache(hour);
        if (savedDataInCache != null) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(savedDataInCache));
            if (isContainReviewInCache((List<ReadReviewListResponse>) objectInputStream.readObject(), reviewId)) {
                redisCacheManager.delete(hour);
            }
        }
    }

    private boolean isContainReviewInCache(List<ReadReviewListResponse> readReviewListResponses, long reviewId) {
        return readReviewListResponses.stream()
                .anyMatch(readReviewListResponse -> readReviewListResponse.getReviewId() == reviewId);
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
