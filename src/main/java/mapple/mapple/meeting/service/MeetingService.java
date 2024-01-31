package mapple.mapple.meeting.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.meeting.dto.CreateMeetingRequest;
import mapple.mapple.meeting.dto.CreateMeetingResponse;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.meeting.entity.UserMeeting;
import mapple.mapple.meeting.repository.MeetingRepository;
import mapple.mapple.meeting.repository.UserMeetingRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final MeetingRepository meetingRepository;

    public CreateMeetingResponse create(CreateMeetingRequest dto, String identifier) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));

        Meeting meeting = Meeting.create(dto.getMeetingName());
        meetingRepository.save(meeting);

        List<User> members = new ArrayList<>();
        for (String email : dto.getMemberEmails()) {
            User member = userRepository.findByIdentifier(email)
                    .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
            UserMeeting userMeeting = UserMeeting.create(member, meeting);
            userMeetingRepository.save(userMeeting);
            members.add(member);
        }
        UserMeeting userMeeting = UserMeeting.create(user, meeting);
        userMeetingRepository.save(userMeeting);
        members.add(user);

        List<String> memberNames = members.stream()
                .map(member -> member.getUsername())
                .toList();
        return new CreateMeetingResponse(meeting.getMeetingName(), memberNames);
    }
}
