package mapple.mapple.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheManager {
    private static final String BEST_REVIEWS_KEY_PREFIX = "best_reviews::";
    private final RedisTemplate<String, byte[]> redisTemplate;

    public byte[] getBestReviewsFromCache(long hour) {
        String key = BEST_REVIEWS_KEY_PREFIX + hour;
        byte[] result = redisTemplate.opsForValue().get(key);
        return result;
    }

    public void delete(long hour) {
        String key = BEST_REVIEWS_KEY_PREFIX + hour;
        redisTemplate.delete(key);
    }
}
