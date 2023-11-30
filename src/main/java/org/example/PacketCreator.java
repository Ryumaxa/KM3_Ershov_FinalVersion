package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketCreator {
    public byte[] create(String data) {

        int len = data.length() + 20 + 8;
        byte TotalLength1 = (byte) (len >> 8 & 255);
        byte TotalLength2 = (byte) (len & 255);
        int SourcePort = 56878;
        int DestinationPort = 1200;

        List<Byte> bytes = new ArrayList<>(Arrays.asList(
                (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 69, (byte) 0, TotalLength1, TotalLength2,
                (byte) 195, (byte) 164, (byte) 0, (byte) 0, (byte) 128, (byte) 17, (byte) 0, (byte) 0,
                (byte) 172, (byte) 20, (byte) 10, (byte) 9, (byte) 172, (byte) 20, (byte) 10, (byte) 9,
                (byte) (SourcePort >> 8 & 255), (byte) (SourcePort & 255),
                (byte) (DestinationPort >> 8 & 255), (byte) (DestinationPort & 255),
                (byte) ((data.length() + 8) >> 8 & 255), (byte) ((data.length() + 8) & 255),
                (byte) 0, (byte) 0
        ));

        for (char c : data.toCharArray()) {
            bytes.add((byte) c);
        }

        byte[] packet = new byte[bytes.size()];
        for (int i = 0; i < packet.length; i++) {
            packet[i] = bytes.get(i);
        }

        return packet;

    }
}