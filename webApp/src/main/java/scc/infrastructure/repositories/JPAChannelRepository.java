package scc.infrastructure.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import scc.application.repositories.ChannelsRepository;
import scc.domain.entities.Channel;
import scc.rest.config.RedisCacheConfig;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@EnableCaching
public class JPAChannelRepository extends SimpleJpaRepository<Channel, String> implements ChannelsRepository {

    public JPAChannelRepository(EntityManager em) {
        super(Channel.class, em);
    }

    @Override
    @Cacheable(RedisCacheConfig.CHANNEL_CACHE)
    public Optional<Channel> findById(String id) {
        return super.findById(id);
    }

    @Override
    @CachePut(value = RedisCacheConfig.CHANNEL_CACHE, key = "#channel.id")
    public <S extends Channel> S save(S channel) {
        return super.save(channel);
    }

    @Override
    @CacheEvict(RedisCacheConfig.CHANNEL_CACHE)
    public void deleteById(String id) {
        super.deleteById(id);
    }

    @Override
    public Channel addUserToChannel(String channelId, String userId) {
        return null;
    }
}
