package org.example;


import lombok.Data;

@Data
public class NameGUIDclass {
    private String name;
    private boolean isguid;

    public NameGUIDclass(String name, boolean isguid) {
        this.name = name;
        this.isguid = isguid;
    }

    public NameGUIDclass() {
    }
}
