package org.example;

import jade.core.AID;
import jade.core.Agent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AgentDetector implements AgentDetectorInterface {

    private RawUdpSocketServer MyServer;
    private boolean run = true;

    @Override
    public void startPublishing(Agent agent, int port) {
        AID aid = agent.getAID();
        RawUdpSocketClient cLient = new RawUdpSocketClient();
        cLient.initialize();
        PacketCreator pk = new PacketCreator();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(()->{
            if(!agent.isAlive()){
                MyServer.setRun(false);
                run = false;
                service.shutdown();
            }
            cLient.send(pk.create(Converter.AidToJsonString(aid)));
        },1000,1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void startDiscovering(int port) {
        MyServer = new RawUdpSocketServer();
        MyServer.start(port);
    }

    public void publishActiveAgent(Agent agent){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(()->{
            if(!run){
                service.shutdown();
            }
            List<AID> activeAgents = getActiveAgents();
            List<String> names = new ArrayList<>();
            for (AID aids : activeAgents) {
                names.add(aids.getLocalName());
            }
            System.out.println(agent.getLocalName() + " считает живыми этих агентов : " + names);
        },1000,1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public List<AID> getActiveAgents() {
        ArrayList<AgentListClass> AgentsList = MyServer.getAgentList();
        List<AID> AliveAgents = new ArrayList<>();
        for (AgentListClass agent : AgentsList) {
            AliveAgents.add(new AID(agent.getName(), agent.isIsguid()));
        }
        return AliveAgents;
    }

}