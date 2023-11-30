package org.example;

import org.pcap4j.core.*;

import java.util.List;

public class RawUdpSocketClient {
    private PcapHandle pcapHandle;

    public void send(byte[] data){
        try {
            pcapHandle.sendPacket(data);
        } catch (NotOpenException | PcapNativeException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize(){
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
        try {
            if (networkInterface != null) {
                pcapHandle = networkInterface.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 50);
            }
        } catch (PcapNativeException e) {
            throw new RuntimeException(e);
        }

    }
}