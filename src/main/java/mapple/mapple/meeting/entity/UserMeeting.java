package mapple.mapple.meeting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.user.entity.User;

@Entity
@Getter
public class UserMeeting {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_meeting_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    public static UserMeeting create(User user, Meeting meeting) {
        UserMeeting userMeeting = new UserMeeting();
        userMeeting.user = user;
        meeting.addUserMeeting(userMeeting); // 연관관계 편의 메소드
        return userMeeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
