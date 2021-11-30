package scc.rest.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    public static final String USER_CACHE = "userCache";
    public static final String CHANNEL_CACHE = "channelCache";
    public static final String MESSAGE_CACHE = "messageCache";
    public static final String MESSAGE_PAGE_CACHE = "messagePageCache";

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(USER_CACHE,
                        cacheConfiguration().entryTtl(Duration.ofMinutes(1)))
                .withCacheConfiguration(CHANNEL_CACHE,
                        cacheConfiguration().entryTtl(Duration.ofMinutes(1)))
                .withCacheConfiguration(MESSAGE_CACHE,
                        cacheConfiguration().entryTtl(Duration.ofMinutes(1)))
                .withCacheConfiguration(MESSAGE_PAGE_CACHE,
                        cacheConfiguration().entryTtl(Duration.ofMinutes(1)));
    }
}
