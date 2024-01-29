package mapple.mapple.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@AllArgsConstructor
@RedisHash(value = "identifier", timeToLive = 60 * 60) // TTL = 60분
public class RefreshToken {

    @Id
    private String identifier;

    private String refreshToken;
}
