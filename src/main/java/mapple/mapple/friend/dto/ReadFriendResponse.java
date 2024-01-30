package mapple.mapple.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.friend.entity.RequestStatus;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ReadFriendResponse {

    private String fromUsername;
    private String toUsername;
    private RequestStatus requestStatus;
}
