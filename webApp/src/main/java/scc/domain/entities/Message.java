package scc.domain.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR_NAME)
    @GenericGenerator(name = Constants.ID_GENERATOR_NAME, strategy = Constants.ID_GENERATOR_NAME)
    private String id;
    private String replyTo;
    private String channel;
    private String user;
    @Lob
    private String text;
    private String imageId;

}
