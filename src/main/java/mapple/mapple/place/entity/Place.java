package mapple.mapple.place.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.entity.BaseEntity;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.entity.Image;
import mapple.mapple.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Place extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private long id;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String content;

    private String url;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<PlaceImage> images = new ArrayList<>();

    // 연관관계 편의 메소드
    public void addImage(PlaceImage image) {
        if (image.getPlace() != null) {
            image.getPlace().getImages().remove(image);
        }
        image.setPlace(this);
        this.images.add(image);
    }

    public static Place create(Meeting meeting, User user, String placeName, String content, String url) {
        Place place = new Place();
        place.meeting = meeting;
        place.user = user;
        place.placeName = placeName;
        place.content = content;
        place.url = url;
        return place;
    }

    public void updateImages(List<MultipartFile> files, String placeImageFileDir) throws IOException {
        this.images.clear();

        for (MultipartFile file : files) {
            String updatedName = file.getOriginalFilename();
            String storedName = getStoredName(updatedName);
            file.transferTo(new File(placeImageFileDir + storedName));

            Image image = Image.create(storedName, updatedName, placeImageFileDir);
            PlaceImage.create(image, this);
        }
    }

    private String getStoredName(String updatedName) {
        int pos = updatedName.lastIndexOf(".");
        String ext = updatedName.substring(pos + 1);
        return UUID.randomUUID() + "." + ext;
    }

    public void update(String placeName, String content, String url) {
        this.placeName = placeName;
        this.content = content;
        this.url = url;
    }
}
