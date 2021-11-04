package scc.domain.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
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
    @GeneratedValue
    private String id;
    private String replyTo;
    @PartitionKey
    private String channel;
    private String user;
    private String text;
    private String imageId;
}
