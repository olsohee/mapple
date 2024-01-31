package mapple.mapple.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    private String phoneNumber;
}
