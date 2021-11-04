package scc.domain.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
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
    @GeneratedValue
    private String id;
    private String name;
    private String owner;
    private boolean publicChannel;
    private List<String> members;
}
