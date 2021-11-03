package scc.domain.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "channels")
public class Channel {

    @Id
    private String id;
    private String name;
    private String owner;
    private boolean publicChannel;
    private String[] members;
}
