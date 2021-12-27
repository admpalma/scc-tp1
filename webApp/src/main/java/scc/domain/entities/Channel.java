package scc.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Channel {

    @GeneratedValue(generator = Constants.ID_GENERATOR_NAME)
    @GenericGenerator(name = Constants.ID_GENERATOR_NAME, strategy = Constants.ID_GENERATOR_NAME)
    @Id
    private String id;
    private String name;
    private String owner;
    private boolean publicChannel;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name=Constants.table_name_1,
            joinColumns=@JoinColumn(name=Constants.JOINT_TABLE_COL2),
            inverseJoinColumns=@JoinColumn(name=Constants.JOINT_TABLE_COL1),
            uniqueConstraints = @UniqueConstraint(columnNames={Constants.JOINT_TABLE_COL1,Constants.JOINT_TABLE_COL2})
    )
    private List<User> members;

}
