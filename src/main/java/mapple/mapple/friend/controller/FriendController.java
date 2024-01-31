package mapple.mapple.friend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.SuccessResponse;
import mapple.mapple.friend.dto.FriendRequestsResponse;
import mapple.mapple.friend.service.FriendService;
import mapple.mapple.jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final JwtUtils jwtUtils;

    @PostMapping("/friend/request/{userId}")
    public ResponseEntity requestFriend(@PathVariable("userId") long userId,
                                                HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        FriendRequestsResponse responseData = friendService.request(identifier, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("친구 요청 성공", responseData));
    }

    @PostMapping("/friend/accept/{friendId}")
    public ResponseEntity acceptFriendRequest(@PathVariable("friendId") long friendId,
                                                      HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        FriendRequestsResponse responseData = friendService.accept(identifier, friendId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("친구 요청 수락", responseData));
    }

    @PostMapping("/friend/refuse/{friendId}")
    public ResponseEntity refuseFriendRequest(@PathVariable("friendId") long friendId,
                                                  HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        friendService.refuse(identifier, friendId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("친구 요청 거절"));
    }
}
