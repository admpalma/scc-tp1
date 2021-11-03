package scc.domain.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String pwd;
    private String photoId;
    private String[] channelIds;
}
