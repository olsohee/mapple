package mapple.mapple.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mapple.mapple.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class JoinResponse {

    private String email;
    private String username;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JoinResponse(User user) {
        this.email = user.getIdentifier();
        this.username = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
