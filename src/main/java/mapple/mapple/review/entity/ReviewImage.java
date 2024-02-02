package mapple.mapple.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.entity.Image;

@Entity
@Getter
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private long id;

    @Embedded
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public static ReviewImage create(Image image, Review review) {
        ReviewImage reviewImage = new ReviewImage();
        reviewImage.image = image;
        review.addImage(reviewImage); // 연관관계 편의 메소드
        return reviewImage;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
