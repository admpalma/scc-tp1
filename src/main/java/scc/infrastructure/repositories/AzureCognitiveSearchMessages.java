package scc.infrastructure.repositories;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.Context;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchClientBuilder;
import com.azure.search.documents.SearchDocument;
import com.azure.search.documents.models.SearchMode;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.util.SearchPagedIterable;
import com.azure.search.documents.util.SearchPagedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import scc.application.repositories.SearchMessages;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class AzureCognitiveSearchMessages implements SearchMessages {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SearchClient searchClient;


    public AzureCognitiveSearchMessages(@Value("${searchKey}") String searchKey,
                                        @Value("${searchUrl}") String searchUrl,
                                        @Value("${searchIndex}") String searchIndex) {
        searchClient = new SearchClientBuilder()
                .credential(new AzureKeyCredential(searchKey))
                .endpoint(searchUrl)
                .indexName(searchIndex)
                .buildClient();
    }

    public List<String> getMessagesWithText(String queryText) {

        SearchOptions options = new SearchOptions()
                .setIncludeTotalCount(true) //Include total number of hits in the index
                .setSearchMode(SearchMode.ALL)
                .setSearchFields("text") // Fields to search
                .setSelect("id", "user", "text"); //Fields to return

        SearchPagedIterable searchPagedIterable = searchClient.search(queryText, options, Context.NONE);


        List<String> list = new LinkedList<>();
        for (SearchPagedResponse resultResponse : searchPagedIterable.iterableByPage()) {
            resultResponse.getValue().forEach(searchResult -> {
                for (Map.Entry<String, Object> res : searchResult
                        .getDocument(SearchDocument.class)
                        .entrySet()) {
                    log.info(res.getKey() + "->" + res.getValue());
                    list.add((String) res.getValue());
                }
            });
        }
        return list;
    }
}
