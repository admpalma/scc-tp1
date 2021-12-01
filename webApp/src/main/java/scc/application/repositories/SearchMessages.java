package scc.application.repositories;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SearchMessages {

    List<String> getMessagesWithText(String queryText);
}
