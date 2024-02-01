package mapple.mapple.place.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.Image;
import mapple.mapple.exception.*;
import mapple.mapple.exception.customException.MeetingException;
import mapple.mapple.exception.customException.PlaceException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.meeting.repository.MeetingRepository;
import mapple.mapple.place.dto.CreateAndUpdatePlaceRequest;
import mapple.mapple.place.dto.CreateAndUpdatePlaceResponse;
import mapple.mapple.place.dto.ReadPlaceListResponse;
import mapple.mapple.place.dto.ReadPlaceResponse;
import mapple.mapple.place.entity.Place;
import mapple.mapple.place.entity.PlaceImage;
import mapple.mapple.place.repository.PlaceRepository;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
import mapple.mapple.validator.MeetingValidator;
import mapple.mapple.validator.PlaceValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingValidator meetingValidator;
    private final PlaceValidator placeValidator;

    @Value("${file.dir.place_image}")
    private String placeImageFileDir;

    public CreateAndUpdatePlaceResponse create(CreateAndUpdatePlaceRequest dto, List<MultipartFile> files, long meetingId, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Meeting meeting = findMeetingById(meetingId);

        meetingValidator.validateMeetingMember(meeting, user);

        Place place = Place.create(meeting, user, dto.getPlaceName(), dto.getContent(), dto.getUrl());
        if (files != null) {
            place.updateImages(files, placeImageFileDir);
        }

        placeRepository.save(place);

        // dto 생성
        List<byte[]> imageByteList = new ArrayList<>();
        for (PlaceImage placeImage : place.getImages()) {
            Image image = placeImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }

        return new CreateAndUpdatePlaceResponse(user.getUsername(), meeting.getMeetingName(), place.getPlaceName(),
                place.getContent(), place.getUrl(), place.getCreatedAt(), place.getUpdatedAt(),
                imageByteList);
    }

    public List<ReadPlaceListResponse> readAll(long meetingId, String identifier) {
        User user = findUserByIdentifier(identifier);
        Meeting meeting = findMeetingById(meetingId);
        meetingValidator.validateMeetingMember(meeting, user);

        return placeRepository.findAll().stream()
                .filter(place -> place.getMeeting().equals(meeting))
                .map(place -> new ReadPlaceListResponse(place.getUser().getUsername(), place.getPlaceName(),
                        place.getCreatedAt(), place.getUpdatedAt()))
                .toList();
    }

    public ReadPlaceResponse readOne(long meetingId, long placeId, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Meeting meeting = findMeetingById(meetingId);
        meetingValidator.validateMeetingMember(meeting, user);

        Place place = findPlaceById(placeId);

        // dto 생성
        List<byte[]> imageByteList = new ArrayList<>();
        for (PlaceImage placeImage : place.getImages()) {
            Image image = placeImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }
        return new ReadPlaceResponse(user.getUsername(), place.getPlaceName(), place.getContent(),
                place.getUrl(), place.getCreatedAt(), place.getUpdatedAt(), imageByteList);
    }

    public CreateAndUpdatePlaceResponse update(CreateAndUpdatePlaceRequest dto, List<MultipartFile> files,
                                               long meetingId, long placeId, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Place place = findPlaceById(placeId);
        Meeting meeting = findMeetingById(meetingId);

        placeValidator.validatePlaceAuthorization(user, place);

        place.update(dto.getPlaceName(), dto.getContent(), dto.getUrl());
        if (files != null) {
            place.updateImages(files, placeImageFileDir);
        }

        // dto 생성
        List<byte[]> imageByteList = new ArrayList<>();
        for (PlaceImage placeImage : place.getImages()) {
            Image image = placeImage.getImage();
            Path path = Paths.get(image.getStoreDir() + image.getStoredName());
            if (Files.probeContentType(path) != null) {
                File file = new File(image.getStoreDir() + image.getStoredName());
                byte[] imageByte = FileCopyUtils.copyToByteArray(file);
                imageByteList.add(imageByte);
            }
        }

        return new CreateAndUpdatePlaceResponse(user.getUsername(), meeting.getMeetingName(), place.getPlaceName(),
                place.getContent(), place.getUrl(), place.getCreatedAt(), place.getUpdatedAt(),
                imageByteList);
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
