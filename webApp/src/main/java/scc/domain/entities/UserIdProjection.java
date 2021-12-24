package scc.domain.entities;


import java.util.List;

public interface UserIdProjection {
    List<UserSummary> getMembers();

    interface UserSummary {
        String getId();
    }
}
