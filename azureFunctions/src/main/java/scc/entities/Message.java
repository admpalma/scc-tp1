package scc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String id;
    private String replyTo;
    private String channel;
    private String user;
    private String text;
    private String imageId;
}
