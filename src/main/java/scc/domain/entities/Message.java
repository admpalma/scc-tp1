package scc.domain.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "messages")
public class Message {

    @Id
    private String id;
    private String replyTo;
    private String channel;
    private String user;
    private String text;
    private String imageId;
}
