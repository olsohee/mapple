package mapple.mapple.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.BaseEntity;
import mapple.mapple.entity.Image;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String content;

    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReviewImage> images = new ArrayList<>();

    public static Review create(String placeName, String content, String url,
                                PublicStatus publicStatus, Rating rating, User user) {
        Review review = new Review();
        review.placeName = placeName;
        review.content = content;
        review.url = url;
        review.publicStatus = publicStatus;
        review.rating = rating;
        review.user = user;
        review.images = new ArrayList<>();
        return review;
    }

    // 연관관계 편의 메소드
    public void addImage(ReviewImage image) {
        if (image.getReview() != null) {
            image.getReview().getImages().remove(image);
        }
        image.setReview(this);
        this.images.add(image);
    }

    public void update(String placeName, String content, Rating rating, PublicStatus publicStatus, String url) {
        this.placeName = placeName;
        this.content = content;
        this.rating = rating;
        this.publicStatus = publicStatus;
        this.url = url;
    }

    public void updateImages(List<MultipartFile> files, String reviewImageFileDir) throws IOException {
        this.images.clear();

        for (MultipartFile file : files) {
            String updatedName = file.getOriginalFilename();
            String storedName = getStoredName(updatedName);
            file.transferTo(new File(reviewImageFileDir + storedName));

            Image image = Image.create(storedName, updatedName, reviewImageFileDir);
            ReviewImage.create(image, this);
        }
    }

    private String getStoredName(String updatedName) {
        int pos = updatedName.lastIndexOf(".");
        String ext = updatedName.substring(pos + 1);
        return UUID.randomUUID() + "." + ext;
    }

    public void deleteImages() {
        this.images.clear();
    }

    public boolean checkIsFriendsReview(List<Friend> friends) {
        return friends.stream()
                .anyMatch(friend -> friend.getToUser() == this.user);
    }

    public boolean isPublic() {
        return publicStatus == PublicStatus.PUBLIC;
    }

    public boolean canRead(List<Friend> friends) {
        return friends.stream()
                .anyMatch(friend -> (friend.getToUser() == this.user) && publicStatus == PublicStatus.ONLY_FRIEND);
    }

    public boolean isPrivate() {
        return publicStatus == PublicStatus.PRIVATE;
    }

    public boolean isOwn(User user) {
        return this.user == user;
    }

    public boolean isOnlyFriend() {
        return publicStatus == PublicStatus.ONLY_FRIEND;
    }
}
