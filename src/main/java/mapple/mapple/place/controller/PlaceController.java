package mapple.mapple.place.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.place.dto.CreatePlaceRequest;
import mapple.mapple.place.dto.CreatePlaceResponse;
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
    private final JwtUtils jwtUtils;

    @PostMapping("/meeting/{meetingId}/place")
    public ResponseEntity create(@Validated @RequestPart CreatePlaceRequest dto,
                                 @RequestPart(required = false) List<MultipartFile> files,
                                 @PathVariable("meetingId") long meetingId,
                                 HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        CreatePlaceResponse responseData = placeService.create(dto, files, meetingId, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseData);
    }
}
