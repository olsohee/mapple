package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ReviewException;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.repository.FriendQueryRepository;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.dto.ReadReviewResponse;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FriendQueryRepository friendQueryRepository;

    @Value("${file.dir.review_image}")
    private String reviewImageFileDir;

    public List<ReadReviewListResponse> readAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getUpdatedAt(), review.getCreatedAt()))
                .toList();
    }

    public List<ReadReviewListResponse> readFriendsReviews(String identifier) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        List<Friend> friends = friendQueryRepository.findFriendsByUser(user);

        List<Review> friendsReviews = reviewRepository.findAll().stream()
                .filter(review -> review.checkIsFriendsReview(friends))
                .toList();
        return friendsReviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getUpdatedAt(), review.getCreatedAt()))
                .toList();
    }

    public ReadReviewResponse read(long reviewId) {
        Review review = findReviewById(reviewId);
        return new ReadReviewResponse(review.getUser().getUsername(), review.getPlaceName(), review.getContent(),
                review.getUrl(), review.getRating(), review.getCreatedAt(), review.getUpdatedAt());
    }

    public CreateAndUpdateReviewResponse createReview(CreateAndUpdateReviewRequest dto, List<MultipartFile> files, String email) throws IOException {
        User user = userRepository.findByIdentifier(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        Review review = Review.create(dto.getPlaceName(), dto.getContent(), dto.getUrl(),
                PublicStatus.find(dto.getPublicStatus()), Rating.find(dto.getRating()), user);

        if (files != null) {
            review.updateImages(files, reviewImageFileDir);
        }

        reviewRepository.save(review);
        return new CreateAndUpdateReviewResponse(review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt());
    }

    public List<ReadReviewListResponse> readAllByUserIdentifier(String identifier) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getUpdatedAt(), review.getCreatedAt()))
                .toList();
    }

    public CreateAndUpdateReviewResponse updateReview(long reviewId, String identifier, CreateAndUpdateReviewRequest dto, List<MultipartFile> files) throws IOException {
        Review review = findReviewById(reviewId);
        validateAuthorization(review, identifier);

        review.update(dto.getPlaceName(), dto.getContent(), Rating.find(dto.getRating()),
                PublicStatus.find(dto.getPublicStatus()), dto.getUrl());

        if (files == null) {
            review.deleteImages();
        } else {
            review.updateImages(files, reviewImageFileDir);
        }

        return new CreateAndUpdateReviewResponse(review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt());
    }

    public void delete(long reviewId, String identifier) {
        Review review = findReviewById(reviewId);
        validateAuthorization(review, identifier);
        reviewRepository.delete(review);
    }

    private Review findReviewById(long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCode.NOT_FOUND_REVIEW));
    }

    private void validateAuthorization(Review review, String identifier) {
        if (!review.getUser().getIdentifier().equals(identifier)) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }
    }
}
