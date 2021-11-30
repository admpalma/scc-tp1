package scc.application.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface SearchMessages {

    List<String> getMessagesWithText(String queryText);
}
