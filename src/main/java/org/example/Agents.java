package org.example;


import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Agents extends Agent {

    protected void setup() {
        log.info(getLocalName() + " успешно очнулся");
        AgentDetector agentDetector =new AgentDetector();
        agentDetector.publishActiveAgent(this);
        agentDetector.startDiscovering(1200);
        agentDetector.startPublishing(this, 1200);
    }

}
