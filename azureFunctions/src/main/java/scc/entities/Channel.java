package scc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class Channel {

    private String id;
    private String name;
    private String owner;
    private boolean publicChannel;
    private List<String> members;
}
