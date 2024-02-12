package mapple.mapple.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.entity.BaseEntity;
import mapple.mapple.user.entity.User;

@Entity
@Getter
public class ReviewLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setReview(Review review) {
        this.review = review;
    }

    public static ReviewLike create(Review review, User user) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.review = review;
        reviewLike.user = user;
        return reviewLike;
    }
}
