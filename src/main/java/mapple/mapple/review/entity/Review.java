package mapple.mapple.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.entity.BaseEntity;
import mapple.mapple.entity.Image;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.entity.Rating;
import mapple.mapple.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
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

    // 연관관계 편의 메소드
    public void addImage(Image image) {
        if (image.getReview() != null) {
            image.getReview().getImages().remove(image);
        }
        image.setReview(this);
        this.images.add(image);
    }
}
