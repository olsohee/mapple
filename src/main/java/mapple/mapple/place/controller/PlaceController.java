package mapple.mapple.place.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.place.dto.CreatePlaceRequest;
import mapple.mapple.place.dto.CreatePlaceResponse;
import mapple.mapple.place.service.PlaceService;
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

    @PostMapping("/place")
    public CreatePlaceResponse createPlace(@Validated @RequestPart CreatePlaceRequest dto,
                                           @RequestPart List<MultipartFile> files,
                                           HttpServletRequest request) throws IOException {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        return placeService.create(dto, files, identifier);
    }
}
