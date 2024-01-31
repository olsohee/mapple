package mapple.mapple.friend.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.FriendException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.friend.dto.ReadFriendResponse;
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

    public ReadFriendResponse request(String identifier, long userId) {
        User fromUser = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
        if (fromUser.getId() == userId) {
            throw new FriendException(ErrorCodeAndMessage.CAN_NOT_FRIEND_SAME_USER);
        }
        User toUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
        Friend friend = Friend.create(fromUser, toUser);
        friendRepository.save(friend);
        return new ReadFriendResponse(friend.getFromUser().getUsername(), friend.getToUser().getUsername(), friend.getRequestStatus());
    }

    public ReadFriendResponse accept(String identifier, long friendId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new FriendException(ErrorCodeAndMessage.NOT_FOUND_FRIEND));
        validateAuthorization(identifier, friend);
        friend.acceptRequest();
        Friend reverseFriend = Friend.createReverse(friend.getFromUser(), friend.getToUser());
        friendRepository.save(reverseFriend);
        return new ReadFriendResponse(friend.getFromUser().getUsername(), friend.getToUser().getUsername(), friend.getRequestStatus());
    }

    public void refuse(String identifier, long friendId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new FriendException(ErrorCodeAndMessage.NOT_FOUND_FRIEND));
        validateAuthorization(identifier, friend);
        friendRepository.delete(friend);
    }

    private void validateAuthorization(String identifier, Friend friend) {
        User fromUser = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
        if (!fromUser.equals(friend.getToUser())) {
            throw new FriendException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }
}
