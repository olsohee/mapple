package mapple.mapple.place.service;

import lombok.RequiredArgsConstructor;
import mapple.mapple.entity.Image;
import mapple.mapple.entity.PublicStatus;
import mapple.mapple.exception.*;
import mapple.mapple.exception.customException.BusinessException;
import mapple.mapple.exception.customException.MeetingException;
import mapple.mapple.exception.customException.UserException;
import mapple.mapple.meeting.entity.Meeting;
import mapple.mapple.meeting.repository.MeetingRepository;
import mapple.mapple.place.dto.CreatePlaceRequest;
import mapple.mapple.place.dto.CreatePlaceResponse;
import mapple.mapple.place.entity.Place;
import mapple.mapple.place.entity.PlaceImage;
import mapple.mapple.place.repository.PlaceRepository;
import mapple.mapple.review.entity.ReviewImage;
import mapple.mapple.user.entity.User;
import mapple.mapple.user.repository.UserRepository;
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

    @Value("${file.dir.place_image}")
    private String placeImageFileDir;

    public CreatePlaceResponse create(CreatePlaceRequest dto, List<MultipartFile> files, long meetingId, String identifier) throws IOException {
        User user = findUserByIdentifier(identifier);
        Meeting meeting = findMeetingById(meetingId);

        validateAuthorization(user, meeting);

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

        return new CreatePlaceResponse(user.getUsername(), meeting.getMeetingName(), place.getPlaceName(),
                place.getContent(), place.getUrl(), place.getCreatedAt(), place.getUpdatedAt(),
                imageByteList);
    }

    private void validateAuthorization(User user, Meeting meeting) {
        boolean hasAuthority = meeting.getUserMeetings().stream()
                .anyMatch(userMeeting -> userMeeting.getUser() == user);
        if (!hasAuthority) {
            throw new BusinessException(ErrorCodeAndMessage.FORBIDDEN);
        }
    }

    private User findUserByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new UserException(ErrorCodeAndMessage.NOT_FOUND_USER));
    }

    private Meeting findMeetingById(long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCodeAndMessage.NOT_FOUND_MEETING));
    }
}
