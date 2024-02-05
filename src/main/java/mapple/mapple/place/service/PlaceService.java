package mapple.mapple.place.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.exception.ErrorCodeAndMessage;
import mapple.mapple.exception.customException.MeetingException;
import mapple.mapple.exception.customException.PlaceException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.meeting.repository.MeetingRepository;
import mapple.mapple.place.dto.CreateAndUpdatePlaceRequest;
import mapple.mapple.place.entity.Place;
import mapple.mapple.place.repository.PlaceRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import mapple.mapple.validator.MeetingValidator;
import mapple.mapple.validator.PlaceValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    private final PlaceValidator placeValidator;
    private final MeetingValidator meetingValidator;

    @Value("${file.dir.place_image}")
    private String placeImageFileDir;

    public long create(CreateAndUpdatePlaceRequest dto, List<MultipartFile> files, long meetingId, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Meeting meeting = findMeetingById(meetingId);

        meetingValidator.validateMeetingMember(meeting, user);

        Place place = Place.create(meeting, user, dto.getPlaceName(), dto.getContent(), dto.getUrl());
        if (files != null) {
            place.updateImages(files, placeImageFileDir);
        }

        placeRepository.save(place);
        return place.getId();
    }

    public long update(CreateAndUpdatePlaceRequest dto, List<MultipartFile> files, long placeId, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Place place = findPlaceById(placeId);

        placeValidator.validatePlaceAuthorization(user, place);

        place.update(dto.getPlaceName(), dto.getContent(), dto.getUrl());
        if (files != null) {
            place.updateImages(files, placeImageFileDir);
        }

        return place.getId();
    }

    public void delete(long placeId, String identifier) {
        User user = findUserByIdentifier(identifier);
        Place place = findPlaceById(placeId);
        placeValidator.validatePlaceAuthorization(user, place);
        placeRepository.delete(place);
    }

    private User findUserByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
    }

    private Meeting findMeetingById(long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCodeAndMessage.NOT_FOUND_MEETING));
    }

    private Place findPlaceById(long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceException(ErrorCodeAndMessage.NOT_FOUND_PLACE));
    }
}
