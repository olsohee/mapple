package mapple.mapple.place.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.Image;
import mapple.mapple.exception.*;
import mapple.mapple.exception.customException.MeetingException;
import mapple.mapple.exception.customException.PlaceException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.meeting.repository.MeetingRepository;
import mapple.mapple.place.dto.CreateAndUpdatePlaceResponse;
import mapple.mapple.place.dto.ReadPlaceListResponse;
import mapple.mapple.place.dto.ReadPlaceResponse;
import mapple.mapple.place.entity.Place;
import mapple.mapple.place.entity.PlaceImage;
import mapple.mapple.place.repository.PlaceRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import mapple.mapple.validator.MeetingValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceQueryService {

    private final PlaceRepository placeRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingValidator meetingValidator;

    public CreateAndUpdatePlaceResponse readCreatedUpdatedPlace(long placeId, long meetingId, String identifier) throws IOException {
        Place place = findPlaceById(placeId);
        Meeting meeting = findMeetingById(meetingId);
        User user = findUserByIdentifier(identifier);
        return new CreateAndUpdatePlaceResponse(user, meeting, place, createImagesByteList(place.getImages()));
    }

    public Page<ReadPlaceListResponse> readAll(long meetingId, String identifier, Pageable pageable) {
        User user = findUserByIdentifier(identifier);
        Meeting meeting = findMeetingById(meetingId);
        meetingValidator.validateMeetingMember(meeting, user);
        Page<Place> pageResult = placeRepository.findAllByMeetingId(pageable, meetingId);
        return pageResult.map(place -> new ReadPlaceListResponse(user, place));
    }

    public ReadPlaceResponse readOne(long meetingId, long placeId, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Meeting meeting = findMeetingById(meetingId);
        meetingValidator.validateMeetingMember(meeting, user);
        Place place = findPlaceById(placeId);
        return new ReadPlaceResponse(user, place, createImagesByteList(place.getImages()));
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

    private List<byte[]> createImagesByteList(List<PlaceImage> images) throws IOException {
        List<byte[]> imageByteList = new ArrayList<>();
        for (PlaceImage placeImage : images) {
            Image image = placeImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }
        return imageByteList;
    }
}
