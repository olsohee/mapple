package mapple.mapple.meeting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.entity.BaseEntity;

@Entity
@Getter
public class Meeting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private long id;

    @Column(nullable = false)
    private String meetingName;

    public static Meeting create(String meetingName) {
        Meeting meeting = new Meeting();
        meeting.meetingName = meetingName;
        return meeting;
    }
}
