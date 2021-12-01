package scc.application.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import scc.domain.entities.RemovedUser;

@Repository
public interface RemovedUserRepository extends CosmosRepository<RemovedUser, String> {
}
