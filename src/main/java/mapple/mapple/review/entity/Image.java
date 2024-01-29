package mapple.mapple.review.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    @Column(nullable = false)
    private String storedName;
    @Column(nullable = false)
    private String updatedName;

    @Column(nullable = false)
    private String storeDir;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public void setReview(Review review) {
        this.review = review;
    }

    public static Image create(String storedName, String updatedName, String storeDir, Review review) {
        Image image = new Image();
        image.storedName = storedName;
        image.updatedName = updatedName;
        image.storeDir = storeDir;
        review.addImage(image); // 연관관계 편의 메소드 사용
        return image;
    }

    public static Image create(String storedName, String updatedName, String storeDir) {
        Image image = new Image();
        image.storedName = storedName;
        image.updatedName = updatedName;
        image.storeDir = storeDir;
        return image;
    }
}
