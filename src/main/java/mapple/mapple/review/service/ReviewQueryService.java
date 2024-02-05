package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.Image;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.ReviewException;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.entity.RequestStatus;
import mapple.mapple.friend.repository.FriendRepository;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.dto.ReadReviewResponse;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.review.entity.Review;
import mapple.mapple.review.dto.CreateAndUpdateReviewResponse;
import mapple.mapple.review.entity.ReviewImage;
import mapple.mapple.review.repository.ReviewRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import mapple.mapple.validator.ReviewValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final ReviewValidator reviewValidator;

    public CreateAndUpdateReviewResponse readCreatedUpdatedReview(long reviewId, String identifier) throws IOException {
        Review review = findReviewById(reviewId);
        User user = findUserByIdentifier(identifier);

        List<byte[]> imageByteList = createImagesByteList(review.getImages());

        return new CreateAndUpdateReviewResponse(user.getUsername(), review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt(), review.getUpdatedAt(), imageByteList);
    }

    public List<ReadReviewListResponse> readReadableAllReviews(String identifier) {
        User user = findUserByIdentifier(identifier);

        Set<Review> reviews = new HashSet<>();
        // 전체 공개인 리뷰
        reviewRepository.findByPublicStatus(PublicStatus.PUBLIC).stream()
                .forEach(review -> reviews.add(review));

        // 유저 자신의 리뷰
        reviewRepository.findByUserId(user.getId()).stream()
                .forEach(review -> reviews.add(review));

        // 유저 친구들의 리뷰
        reviewRepository.findFriendReviewsByUserId(user.getId(), PublicStatus.ONLY_FRIEND, RequestStatus.ACCEPT).stream()
                .forEach(review -> reviews.add(review));

        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getCreatedAt(), review.getUpdatedAt()))
                .toList();
    }

    public List<ReadReviewListResponse> readFriendsReviews(String identifier) {
        User user = findUserByIdentifier(identifier);

        Set<Review> reviews = new HashSet<>();
        reviewRepository.findFriendReviewsByUserId(user.getId(), PublicStatus.PUBLIC, RequestStatus.ACCEPT).stream()
                .forEach(review -> reviews.add(review));
        reviewRepository.findFriendReviewsByUserId(user.getId(), PublicStatus.ONLY_FRIEND, RequestStatus.ACCEPT).stream()
                .forEach(review -> reviews.add(review));

        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getCreatedAt(), review.getUpdatedAt()))
                .toList();
    }

    public ReadReviewResponse readOne(long reviewId, String identifier) throws IOException {
        Review review = findReviewById(reviewId);
        User user = findUserByIdentifier(identifier);
        List<Friend> friends = friendRepository.findFriendsByUser(user, RequestStatus.ACCEPT);

        reviewValidator.validateCanRead(review, user, friends);

        List<byte[]> imageByteList = createImagesByteList(review.getImages());

        return new ReadReviewResponse(review.getUser().getUsername(), review.getPlaceName(), review.getContent(),
                review.getUrl(), review.getRating(), review.getCreatedAt(), review.getUpdatedAt(), imageByteList);
    }

    public List<ReadReviewListResponse> readAllByUserIdentifier(String identifier) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
        List<Review> reviews = reviewRepository.findByUserId(user.getId());
        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getCreatedAt(), review.getUpdatedAt()))
                .toList();
    }

    private User findUserByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
    }

    private Review findReviewById(long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCodeAndMessage.NOT_FOUND_REVIEW));
    }

    private List<byte[]> createImagesByteList(List<ReviewImage> images) throws IOException {
        List<byte[]> imageByteList = new ArrayList<>();
        for (ReviewImage reviewImage : images) {
            Image image = reviewImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }
        return imageByteList;
    }
}
