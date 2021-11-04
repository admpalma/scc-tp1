package scc.application.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import scc.domain.entities.Message;

import java.util.List;

@Repository
public interface MessagesRepository extends CosmosRepository<Message, String> {

    @Query("select * from Messages where Messages.channel = @channelId offset @offset limit @limit")
    List<Message> findPageByChannel(@Param("channelId") String channelId, @Param("offset") int offset, @Param("limit") int limit);

}
