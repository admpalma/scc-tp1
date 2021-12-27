package scc.domain.entities;


import java.util.List;

public interface ChannelIdProjection {
    List<ChannelSummary> getChannelIds();

    interface ChannelSummary {
        String getId();
    }
}
