package mapple.mapple.place.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.place.dto.CreatePlaceRequest;
import mapple.mapple.place.dto.CreatePlaceResponse;
import mapple.mapple.place.service.PlaceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final JwtUtils jwtUtils;

    @PostMapping("/place")
    public CreatePlaceResponse createPlace(@Validated @RequestBody CreatePlaceRequest dto,
                                           HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        return placeService.create(dto, identifier);
    }
}
