package org.example;

import jade.core.AID;
import jade.core.Agent;

import java.util.List;

interface AgentDetectorInterface {
    void startPublishing(Agent agent, int port);
    void startDiscovering(int port);
    List<AID> getActiveAgents();
}