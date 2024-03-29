package mapple.mapple.friend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mapple.mapple.user.entity.User;

@Entity
@Getter
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    public static Friend create(User fromUser, User toUser) {
        Friend friend = new Friend();
        friend.fromUser = fromUser;
        friend.toUser = toUser;
        friend.requestStatus = RequestStatus.REQUEST;
        return friend;
    }

    public static Friend createReverse(User fromUser, User toUser) {
        Friend friend = new Friend();
        friend.fromUser = toUser;
        friend.toUser = fromUser;
        friend.requestStatus = RequestStatus.ACCEPT;
        return friend;
    }

    public void acceptRequest() {
        this.requestStatus = RequestStatus.ACCEPT;
    }
}
