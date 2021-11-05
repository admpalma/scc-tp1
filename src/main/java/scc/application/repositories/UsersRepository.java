package scc.application.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import scc.application.dto.UserLoginDto;
import scc.domain.entities.User;

import java.util.Optional;

@Repository
public interface UsersRepository extends CosmosRepository<User, String> {

    Optional<UserLoginDto> getById(String id);
}
