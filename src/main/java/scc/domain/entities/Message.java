package scc.domain.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Container(containerName = "Messages")
public class Message {

    @Id
    private String id;
    private String replyTo;
    private String channel;
    private String user;
    private String text;
    private String imageId;
}
