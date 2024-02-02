package mapple.mapple.place.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.entity.Image;

@Entity
@Getter
public class PlaceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_image_id")
    private long id;

    @Embedded
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    public static PlaceImage create(Image image, Place place) {
        PlaceImage placeImage = new PlaceImage();
        placeImage.image = image;
        place.addImage(placeImage); // 연관관계 편의 메소드
        return placeImage;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
