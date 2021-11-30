package scc.application.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import scc.domain.entities.Message;
import scc.rest.config.RedisCacheConfig;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface MessagesRepository extends CosmosRepository<Message, String> {

    @Override
    @Cacheable(RedisCacheConfig.MESSAGE_CACHE)
    Optional<Message> findById(String id);

    @Override
    @CachePut(value = RedisCacheConfig.MESSAGE_CACHE, key = "#message.id")
    <S extends Message> S save(S message);

    @Override
    @CacheEvict(RedisCacheConfig.MESSAGE_CACHE)
    void deleteById(String id);

    @Query("select * from Messages where Messages.channel = @channelId offset @offset limit @limit")
    @Cacheable(RedisCacheConfig.MESSAGE_PAGE_CACHE) //TODO
    List<Message> findPageByChannel(@Param("channelId") String channelId, @Param("offset") int offset, @Param("limit") int limit);

}
