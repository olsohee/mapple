package mapple.mapple.meeting.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mapple.mapple.SuccessResponse;
import mapple.mapple.jwt.JwtUtils;
import mapple.mapple.meeting.dto.CreateMeetingRequest;
import mapple.mapple.meeting.dto.CreateMeetingResponse;
import mapple.mapple.meeting.service.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final JwtUtils jwtUtils;

    @PostMapping("/meeting")
    public ResponseEntity create(@Validated @RequestBody CreateMeetingRequest dto,
                                        HttpServletRequest request) {
        String identifier = jwtUtils.getIdentifierFromHeader(request);
        CreateMeetingResponse responseData = meetingService.create(dto, identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("모임 생성 성공", responseData));
    }
}
