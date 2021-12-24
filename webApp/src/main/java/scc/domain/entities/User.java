package scc.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR_NAME)
    @GenericGenerator(name = Constants.ID_GENERATOR_NAME, strategy = Constants.ID_GENERATOR_NAME)
    private String id;
    private String name;
    private String pwd;
    private String photoid;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @JsonIgnore
    @ManyToMany //(targetEntity = ChannelEntity.class)
    @JoinTable(name=Constants.table_name_1,
            joinColumns=@JoinColumn(name=Constants.JOINT_TABLE_COL1),
            inverseJoinColumns=@JoinColumn(name=Constants.JOINT_TABLE_COL2),
            uniqueConstraints = @UniqueConstraint(columnNames={Constants.JOINT_TABLE_COL1,Constants.JOINT_TABLE_COL2})
    )
    private List<Channel> channelids;
}
