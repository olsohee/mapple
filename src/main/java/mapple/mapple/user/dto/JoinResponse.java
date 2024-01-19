package mapple.mapple.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponse {

    private String email;
    private LocalDateTime createdAt;
}
