package scc.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    private String id;
    private String name;
    private String pwd;
    private String photoId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = Constants.table_name_1,
            joinColumns = @JoinColumn(name = Constants.JOINT_TABLE_COL1),
            inverseJoinColumns = @JoinColumn(name = Constants.JOINT_TABLE_COL2),
            uniqueConstraints = @UniqueConstraint(columnNames = {Constants.JOINT_TABLE_COL1, Constants.JOINT_TABLE_COL2})
    )
    private List<Channel> channelIds;
}
