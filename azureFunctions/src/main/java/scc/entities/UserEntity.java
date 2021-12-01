package scc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserEntity {
	private String id;
	private String name;
	private String pwd;
	private String photoId;
	private List<String> channelIds;
}
