package scc.application.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scc.domain.entities.Channel;
import scc.domain.entities.UserIdProjection;
import scc.rest.config.RedisCacheConfig;

import java.util.Optional;

@Repository
public interface ChannelsRepository extends JpaRepository<Channel, String> {
    @Override
    @Cacheable(RedisCacheConfig.CHANNEL_CACHE)
    Optional<Channel> findById(String id);

    @Override
    @CachePut(value = RedisCacheConfig.CHANNEL_CACHE, key = "#channel.id")
    <S extends Channel> S save(S channel);

    @Override
    @CacheEvict(RedisCacheConfig.CHANNEL_CACHE)
    void deleteById(String id);

    UserIdProjection findMemberidsById(String id);
}
