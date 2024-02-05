package mapple.mapple.place.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.SuccessResponse;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.place.dto.CreateAndUpdatePlaceRequest;
import mapple.mapple.place.dto.CreateAndUpdatePlaceResponse;
import mapple.mapple.place.dto.ReadPlaceListResponse;
import mapple.mapple.place.dto.ReadPlaceResponse;
import mapple.mapple.place.service.PlaceQueryService;
import mapple.mapple.place.service.PlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceQueryService placeQueryService;
    private final JwtUtils jwtUtils;

    @PostMapping("/meeting/{meetingId}/place")
    public ResponseEntity create(@Validated @RequestPart CreateAndUpdatePlaceRequest dto,
                                 @RequestPart(required = false) List<MultipartFile> files,
                                 @PathVariable("meetingId") long meetingId,
                                 HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        long createdPlaceId = placeService.create(dto, files, meetingId, identifier);
        CreateAndUpdatePlaceResponse responseData = placeQueryService.readCreatedUpdatedPlace(createdPlaceId, meetingId, identifier);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("플레이스 생성 성공", responseData));
    }

    @PutMapping("/meeting/{meetingId}/place/{placeId}")
    public ResponseEntity update(@Validated @RequestPart CreateAndUpdatePlaceRequest dto,
                                 @RequestPart(required = false) List<MultipartFile> files,
                                 @PathVariable("meetingId") long meetingId,
                                 @PathVariable("placeId") long placeId,
                                 HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        placeService.update(dto, files, placeId, identifier);
        CreateAndUpdatePlaceResponse responseData = placeQueryService.readCreatedUpdatedPlace(placeId, meetingId, identifier);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("플레이스 수정 성공", responseData));
    }

    @DeleteMapping("/meeting/{meetingId}/place/{placeId}")
    public ResponseEntity delete(@PathVariable("placeId") long placeId, HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        placeService.delete(placeId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("플레이스 삭제 성공"));
    }

    @GetMapping("/meeting/{meetingId}/places")
    public ResponseEntity readAll(@PathVariable("meetingId") long meetingId, HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        List<ReadPlaceListResponse> responseData = placeQueryService.readAll(meetingId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("플레이스 리스트 조회 성공", responseData));
    }

    @GetMapping("/meeting/{meetingId}/place/{placeId}")
    public ResponseEntity readOne(@PathVariable("meetingId") long meetingId,
                                  @PathVariable("placeId") long placeId,
                                  HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        ReadPlaceResponse responseData = placeQueryService.readOne(meetingId, placeId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("플레이스 단일 조회 성공", responseData));
    }
}
