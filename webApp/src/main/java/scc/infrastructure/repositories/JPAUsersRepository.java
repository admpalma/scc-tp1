package scc.infrastructure.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import scc.application.repositories.UsersChannelsProjectionRepository;
import scc.application.repositories.UsersRepository;
import scc.domain.entities.Channel;
import scc.domain.entities.ChannelIdProjection;
import scc.domain.entities.User;
import scc.rest.config.RedisCacheConfig;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@EnableCaching
public class JPAUsersRepository extends SimpleJpaRepository<User, String> implements UsersRepository {

    private final UsersChannelsProjectionRepository channelsProjections;
    public JPAUsersRepository(EntityManager em, UsersChannelsProjectionRepository channelsProjections) {
        super(User.class, em);
        this.channelsProjections = channelsProjections;
    }

    @Override
    @Cacheable(RedisCacheConfig.USER_CACHE)
    public Optional<User> findById(String id) {
        return super.findById(id);
    }

    @Override
    @CachePut(value = RedisCacheConfig.USER_CACHE, key = "#user.id")
    public <S extends User> S save(S user) {
        return super.save(user);
    }

    @Override
    @CacheEvict(RedisCacheConfig.USER_CACHE)
    public void deleteById(String id) {
        super.deleteById(id);
    }

    @Override
    public Channel subscribeToChannel(String userId, String channelId) {
        return null;
    }

    @Override
    public ChannelIdProjection findChannelidsById(String id) {
        return channelsProjections.findChannelidsById(id);
    }

}
