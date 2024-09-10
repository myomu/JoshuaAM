package site.joshua.am;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory, ResourceLoader resourceLoader) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues() // null 값 캐싱을 비활성화 (Redis 에 null 값을 저장하지 않도록)
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        // Redis 캐시의 구성 정보를 담는 맵
        Map<String, RedisCacheConfiguration> redisCacheConfigMap = new HashMap<>();

        // entryTtl()을 호출하여 캐시 항목의 만료 시간(TTL)을 설정 - 캐시 수명 4시간
        redisCacheConfigMap.put(CacheNames.USERBYUSERID, defaultConfig.entryTtl(Duration.ofHours(4)));

        // ALLUSERS 는 다른 Serializer 적용
        redisCacheConfigMap.put(
                CacheNames.ALLUSERS,
                defaultConfig.entryTtl(Duration.ofHours(4))
                        .serializeValuesWith( //serializeValuesWith()를 호출하여 값을 직렬화하는 방식을 설정
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(new GenericJackson2JsonRedisSerializer())
                        )
        );

        redisCacheConfigMap.put(CacheNames.LOGINUSER, defaultConfig.entryTtl(Duration.ofHours(2)));

        return RedisCacheManager
                .builder(connectionFactory)
                .withInitialCacheConfigurations(redisCacheConfigMap)
                .build();
    }
}
