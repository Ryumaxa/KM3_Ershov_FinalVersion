package org.example;

import com.sun.jna.NativeLibrary;
import lombok.Data;
import lombok.Getter;
import org.pcap4j.core.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class RawUdpSocketServer {
    static {
        if (System.getProperty("os.name").toLowerCase().contains("win")) { // Чтобы работало на винде
            NativeLibrary.addSearchPath("wpcap", "C:\\Windows\\System32\\Npcap");
        }
    }

    protected boolean run = true;
    @Getter
    private ArrayList<AgentListClass> AgentList = new ArrayList<>();


    public void start(int port){
        run = true;
        List<PcapNetworkInterface> allDevs = null;
        try {
            allDevs = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            throw new RuntimeException(e);
        }
        PcapNetworkInterface networkInterface = null;
        for (PcapNetworkInterface allDev : allDevs) {
            if (allDev.getName().equals("\\Device\\NPF_Loopback")){
                networkInterface = allDev;
                break;
            }
        }

        PcapHandle pcapHandle = null;
        try {
            if (networkInterface != null) {
                pcapHandle = networkInterface.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 50);
            }
        } catch (PcapNativeException e) {
            throw new RuntimeException(e);
        }
        try {
            if (pcapHandle != null) {
                pcapHandle.setFilter("ip proto \\udp && dst port "+port, BpfProgram.BpfCompileMode.NONOPTIMIZE);
            }
        } catch (PcapNativeException | NotOpenException e) {
            throw new RuntimeException(e);
        }

        runInThread(pcapHandle);
    }

    protected void runInThread(PcapHandle pcapHandle) {

        Thread thread1 = new Thread(() -> grabPackets(pcapHandle));
        thread1.start();
    }

    protected void grabPackets(PcapHandle pcapHandle) {
        try {
            pcapHandle.loop(0, (PacketListener) packet -> {
                byte[] rawData = packet.getRawData();
                byte[] data = new byte[rawData.length-32];
                System.arraycopy(rawData, 32, data, 0, data.length);
                String agent_data_str = new String(data);
                NameGUIDclass agent_data = Converter.JsonStringToAid(agent_data_str);

                long timestamp = System.currentTimeMillis();

                boolean is_found = false;
                int counter = 0;

                for (AgentListClass agent : AgentList) {
                    if (agent.getName().equals(agent_data.getName())) {
                        is_found = true;
                        break;
                    }
                    counter++;
                }

                if (!is_found) {
                    AgentList.add(new AgentListClass(agent_data.getName(), agent_data.isIsguid(), timestamp));
                } else {
                    AgentList.get(counter).setTimestamp(timestamp);
                }
                long timestamp1 = System.currentTimeMillis();

                AgentList.removeIf(agents -> (timestamp1 - agents.getTimestamp()) > 2000 && agents.getTimestamp() != 0);

                if (!run){
                    try {
                        pcapHandle.breakLoop();
                    } catch (NotOpenException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (PcapNativeException | InterruptedException | NotOpenException e) {
            throw new RuntimeException(e);
        }
    }


}
