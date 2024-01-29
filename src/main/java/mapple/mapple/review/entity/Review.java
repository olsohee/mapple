package mapple.mapple.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.BaseEntity;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.entity.Rating;
import mapple.mapple.user.entity.User;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<Image> images = new ArrayList<>();

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
    public void addImage(Image image) {
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

    public void updateImages(List<Image> images) {
        this.images.clear();
        for (Image image : images) {
            this.images.add(image);
        }
    }
}
