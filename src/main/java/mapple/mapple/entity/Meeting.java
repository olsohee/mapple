package mapple.mapple.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Meeting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private long id;

    @Column(nullable = false)
    private String meetingName;
}
