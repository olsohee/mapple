package mapple.mapple.friend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.friend.dto.RequestFriendResponse;
import mapple.mapple.friend.service.FriendService;
import mapple.mapple.jwt.JwtUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final JwtUtils jwtUtils;

    @PostMapping("friend/request/{friendId}")
    public RequestFriendResponse requestFriend(@PathVariable("friendId") long friendId,
                                               HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        return friendService.request(identifier, friendId);
    }
}
