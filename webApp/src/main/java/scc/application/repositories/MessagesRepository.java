package scc.application.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scc.domain.entities.Message;
import scc.rest.config.RedisCacheConfig;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface MessagesRepository extends JpaRepository<Message,String> {

    @Override
    @Cacheable(RedisCacheConfig.MESSAGE_CACHE)
    Optional<Message> findById(String id);

    @Override
    @CachePut(value = RedisCacheConfig.MESSAGE_CACHE, key = "#message.id")
    <S extends Message> S save(S message);

    @Override
    @CacheEvict(RedisCacheConfig.MESSAGE_CACHE)
    void deleteById(String id);

    //@Query("select * from Messages where Messages.channel = @channelId offset @offset limit @limit")
    @Cacheable(RedisCacheConfig.MESSAGE_PAGE_CACHE) //TODO
    List<Message> findByChannel(String channel, Pageable pageable);
}
