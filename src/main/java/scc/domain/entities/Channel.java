package scc.domain.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Container(containerName = "Channels")
public class Channel {

    @Id
    private String id;
    private String name;
    private String owner;
    private boolean publicChannel;
    private List<String> members;
}
