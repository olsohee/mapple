package mapple.mapple.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthJoinRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String username;
}
