package mapple.mapple.place.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.entity.BaseEntity;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.meeting.entity.Meeting;

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

    @Column(nullable = false)
    private PublicStatus publicStatus;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Meeting meeting;

    public static Place create(Meeting meeting, String placeName, String content,
                              String url, PublicStatus publicStatus) {
        Place place = new Place();
        place.meeting = meeting;
        place.placeName = placeName;
        place.content = content;
        place.url = url;
        place.publicStatus = publicStatus;
        return place;
    }
}
