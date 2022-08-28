package xyz.edge.ac.packetevents.packetwrappers.api;

public interface SendableWrapper
{
    Object asNMSPacket() throws Exception;
}
