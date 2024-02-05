package mapple.mapple.friend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.entity.RequestStatus;

@NoArgsConstructor
@Getter
public class FriendRequestsResponse {

    private String fromUsername;
    private String toUsername;
    private RequestStatus requestStatus;

    public FriendRequestsResponse(Friend friend) {
        this.fromUsername = friend.getFromUser().getUsername();
        this.toUsername = friend.getToUser().getUsername();
        this.requestStatus = friend.getRequestStatus();
    }
}
