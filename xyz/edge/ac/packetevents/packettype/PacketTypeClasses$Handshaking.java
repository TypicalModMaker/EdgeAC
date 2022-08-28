package xyz.edge.ac.packetevents.packettype;

import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public static class Handshaking
{
    public static class Client
    {
        private static String PREFIX;
        public static Class<?> SET_PROTOCOL;
        
        public static void load() {
            if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                Client.PREFIX = "net.minecraft.network.protocol.handshake.";
            }
            else {
                Client.PREFIX = ServerVersion.getNMSDirectory() + ".";
            }
            Client.SET_PROTOCOL = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketHandshakingInSetProtocol");
        }
    }
}
