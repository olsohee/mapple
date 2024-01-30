package mapple.mapple.meeting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Meeting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private long id;

    @Column(nullable = false)
    private String meetingName;

    @OneToMany
    List<UserMeeting> userMeetings = new ArrayList<>();

    public static Meeting create(String meetingName) {
        Meeting meeting = new Meeting();
        meeting.meetingName = meetingName;
        return meeting;
    }

    // 연관관계 편의 메소드
    public void addUserMeeting(UserMeeting userMeeting) {
        if (userMeeting.getMeeting() != null) {
            userMeeting.getMeeting().getUserMeetings().remove(userMeeting);
        }
        userMeetings.add(userMeeting);
        userMeeting.setMeeting(this);
    }
}
