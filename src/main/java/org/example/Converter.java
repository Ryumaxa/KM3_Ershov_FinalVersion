package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;

public abstract class Converter {
    public static String AidToJsonString(AID aid) {

        boolean isGuid;
        if (aid.getLocalName().equals(aid.getName())) {
            isGuid = true;
        } else {
            isGuid = false;
        }
        String name = aid.getLocalName();

        NameGUIDclass AgentData = new NameGUIDclass(name, isGuid);

        String jsonString;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonString = objectMapper.writeValueAsString(AgentData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    public static NameGUIDclass JsonStringToAid (String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        NameGUIDclass myObject = null;
        try {
            myObject = objectMapper.readValue(jsonString, NameGUIDclass.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return myObject;
    }
}

