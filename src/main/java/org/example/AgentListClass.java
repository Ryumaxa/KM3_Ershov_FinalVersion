package org.example;

import lombok.Data;

@Data
public class AgentListClass {
    private String name;
    private boolean isguid;
    private long timestamp;

    public AgentListClass(String name, boolean isguid, long timestamp) {
        this.name = name;
        this.isguid = isguid;
        this.timestamp = timestamp;
    }

}
