package mapple.mapple.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.user.entity.OAuthProvider;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponse {

    private String email;
    private String username;
    private String phoneNumber;
    private OAuthProvider oAuthProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
