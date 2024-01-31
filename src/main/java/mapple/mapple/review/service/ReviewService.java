package mapple.mapple.review.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.Image;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.BusinessException;
import mapple.mapple.exception.customException.ReviewException;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.repository.FriendQueryRepository;
import mapple.mapple.review.dto.ReadReviewListResponse;
import mapple.mapple.review.dto.ReadReviewResponse;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.review.entity.Rating;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.review.entity.Review;
import mapple.mapple.review.dto.CreateAndUpdateReviewRequest;
import mapple.mapple.review.dto.CreateAndUpdateReviewResponse;
import mapple.mapple.review.entity.ReviewImage;
import mapple.mapple.review.repository.ReviewRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public CreateAndUpdateReviewResponse create(CreateAndUpdateReviewRequest dto, List<MultipartFile> files, String email) throws IOException {
        User user = userRepository.findByIdentifier(email)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));

        Review review = Review.create(dto.getPlaceName(), dto.getContent(), dto.getUrl(),
                PublicStatus.find(dto.getPublicStatus()), Rating.find(dto.getRating()), user);

        if (files != null) {
            review.updateImages(files, reviewImageFileDir);
        }

        reviewRepository.save(review);

        // dto 생성
        List<byte[]> imageByteList = new ArrayList<>();
        for (ReviewImage reviewImage : review.getImages()) {
            Image image = reviewImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }

        return new CreateAndUpdateReviewResponse(user.getUsername(), review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt(), review.getUpdatedAt(), imageByteList);
    }

    public List<ReadReviewListResponse> readReadableAllReviews(String identifier) {
        User user = findUserByIdentifier(identifier);
        List<Friend> friends = friendQueryRepository.findFriendsByUser(user);

        // 자신의 리뷰 + 전체 공개 + 친구이면 친구 공개인 리뷰
        List<Review> reviews = reviewRepository.findAll().stream()
                .filter(review -> review.isPublic() || review.canRead(friends) || review.isOwn(user))
                .toList();

        return reviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getCreatedAt(), review.getUpdatedAt()))
                .toList();
    }

    public List<ReadReviewListResponse> readFriendsReviews(String identifier) {
        User user = findUserByIdentifier(identifier);
        List<Friend> friends = friendQueryRepository.findFriendsByUser(user);

        List<Review> friendsReviews = reviewRepository.findAll().stream()
                .filter(review -> review.checkIsFriendsReview(friends) && !review.isPrivate())
                .toList();
        return friendsReviews.stream()
                .map(review -> new ReadReviewListResponse(review.getUser().getUsername(), review.getPlaceName(),
                        review.getRating(), review.getCreatedAt(), review.getUpdatedAt()))
                .toList();
    }

    public ReadReviewResponse readOne(long reviewId, String identifier) throws IOException {
        Review review = findReviewById(reviewId);
        User user = findUserByIdentifier(identifier);
        validateReadAuthorization(review, user);

        // dto 생성
        List<byte[]> imageByteList = new ArrayList<>();
        for (ReviewImage reviewImage : review.getImages()) {
            Image image = reviewImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }
        return new ReadReviewResponse(review.getUser().getUsername(), review.getPlaceName(), review.getContent(),
                review.getUrl(), review.getRating(), review.getCreatedAt(), review.getUpdatedAt(), imageByteList);
    }

    private void validateReadAuthorization(Review review, User user) {
        if (review.getUser().equals(user)) {
            return;
        }
        if (review.isPrivate()) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        } else if (review.isOnlyFriend()) {
            if (!isFriend(review.getUser(), user)) {
                throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
            }
        }
    }

    private boolean isFriend(User reviewUser, User readUser) {
        List<Friend> friends = friendQueryRepository.findFriendsByUser(reviewUser);
        return friends.stream()
                .anyMatch(friend -> friend.getToUser().equals(readUser));
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

    public CreateAndUpdateReviewResponse updateReview(long reviewId, String identifier, CreateAndUpdateReviewRequest dto, List<MultipartFile> files) throws IOException {
        Review review = findReviewById(reviewId);
        User user = findUserByIdentifier(identifier);
        validateOwner(review, user);

        review.update(dto.getPlaceName(), dto.getContent(), Rating.find(dto.getRating()),
                PublicStatus.find(dto.getPublicStatus()), dto.getUrl());

        if (files == null) {
            review.deleteImages();
        } else {
            review.updateImages(files, reviewImageFileDir);
        }

        // dto 생성
        List<byte[]> imageByteList = new ArrayList<>();
        for (ReviewImage reviewImage : review.getImages()) {
            Image image = reviewImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }

        return new CreateAndUpdateReviewResponse(user.getUsername(), review.getPlaceName(), review.getContent(), review.getUrl(),
                review.getPublicStatus(), review.getRating(), review.getCreatedAt(), review.getUpdatedAt(), imageByteList);
    }

    public void delete(long reviewId, String identifier) {
        Review review = findReviewById(reviewId);
        User user = findUserByIdentifier(identifier);
        validateOwner(review, user);
        reviewRepository.delete(review);
    }

    private User findUserByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
    }

    private Review findReviewById(long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCodeAndMessage.NOT_FOUND_REVIEW));
    }

    private void validateOwner(Review review, User user) {
        if (!review.getUser().equals(user)) {
            throw new UserException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }
}
