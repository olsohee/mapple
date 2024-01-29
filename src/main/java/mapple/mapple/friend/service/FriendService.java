package mapple.mapple.friend.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.UserException;
import mapple.mapple.friend.dto.RequestFriendResponse;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.repository.FriendRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public RequestFriendResponse request(String identifier, long friendId) {
        User fromUser = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
        if (fromUser.getId() == friendId) {
            throw new UserException(ErrorCode.CAN_NOT_FRIEND_SAME_USER);
        }
        User toUser = userRepository.findById(friendId)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
        Friend friend = Friend.create(fromUser, toUser);
        friendRepository.save(friend);
        return new RequestFriendResponse(fromUser.getUsername(), toUser.getUsername(), friend.getRequestStatus());
    }
}
