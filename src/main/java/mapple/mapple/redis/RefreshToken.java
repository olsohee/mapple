package mapple.mapple.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@AllArgsConstructor
@RedisHash(value = "identifier", timeToLive = 60 * 60 * 24 * 30) // TTL = 30일
public class RefreshToken {

    @Id
    private String identifier;

    private String refreshToken;
}
