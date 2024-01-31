package mapple.mapple.place.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.exception.BusinessException;
import mapple.mapple.exception.ErrorCode;
import mapple.mapple.exception.MeetingException;
import mapple.mapple.exception.UserException;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.meeting.repository.MeetingRepository;
import mapple.mapple.place.dto.CreatePlaceRequest;
import mapple.mapple.place.dto.CreatePlaceResponse;
import mapple.mapple.place.entity.Place;
import mapple.mapple.place.repository.PlaceRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    @Value("${file.dir.place_image}")
    private String placeImageFileDir;

    public CreatePlaceResponse create(CreatePlaceRequest dto, List<MultipartFile> files, String identifier) throws IOException {
        Meeting meeting = meetingRepository.findById(dto.getMeetingId())
                .orElseThrow(() -> new MeetingException(ErrorCode.NOT_FOUND_MEETING));
        validateAuthorization(identifier, meeting);

        Place place = Place.create(meeting, dto.getPlaceName(), dto.getContent(),
                dto.getUrl(), PublicStatus.find(dto.getPublicStatus()));
        if (files != null) {
            place.updateImages(files, placeImageFileDir);
        }

        placeRepository.save(place);

        return new CreatePlaceResponse(meeting.getMeetingName(),
                place.getPlaceName(), place.getContent(), place.getUrl(), place.getPublicStatus(), place.getCreatedAt());
    }

    private void validateAuthorization(String identifier, Meeting meeting) {
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

        boolean hasAuthority = meeting.getUserMeetings().stream()
                .anyMatch(userMeeting -> userMeeting.getUser() == user);
        if (!hasAuthority) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}
