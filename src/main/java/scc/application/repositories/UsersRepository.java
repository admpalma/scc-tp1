package scc.application.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import scc.domain.entities.User;

@Repository
public interface UsersRepository extends CosmosRepository<User, String> {
}
