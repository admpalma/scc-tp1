package scc.domain.entities;


import java.util.List;

public interface ChannelIdProjection {
    List<ChannelSummary> getChannelids();

    interface ChannelSummary {
        String getId();
    }
}
