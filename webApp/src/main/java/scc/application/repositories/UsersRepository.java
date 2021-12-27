package scc.application.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scc.domain.entities.ChannelIdProjection;
import scc.domain.entities.User;
import scc.rest.config.RedisCacheConfig;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {
    @Override
    @Cacheable(RedisCacheConfig.USER_CACHE)
    Optional<User> findById(String id);

    @Override
    @CachePut(value = RedisCacheConfig.USER_CACHE, key = "#user.id")
    <S extends User> S save(S user);

    @Override
    @CacheEvict(RedisCacheConfig.USER_CACHE)
    void deleteById(String id);

    ChannelIdProjection findChannelidsById(String id);

}
