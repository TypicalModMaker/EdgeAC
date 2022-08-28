package xyz.edge.ac.packetevents.packettype;

import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public static class Status
{
    public static class Client
    {
        private static String PREFIX;
        public static Class<?> START;
        public static Class<?> PING;
        
        public static void load() {
            if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                Client.PREFIX = "net.minecraft.network.protocol.status.";
            }
            else {
                Client.PREFIX = ServerVersion.getNMSDirectory() + ".";
            }
            Client.START = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketStatusInStart");
            Client.PING = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketStatusInPing");
        }
    }
    
    public static class Server
    {
        private static String PREFIX;
        public static Class<?> PONG;
        public static Class<?> SERVER_INFO;
        
        public static void load() {
            if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
                Server.PREFIX = "net.minecraft.network.protocol.status.";
            }
            else {
                Server.PREFIX = ServerVersion.getNMSDirectory() + ".";
            }
            Server.PONG = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketStatusOutPong");
            Server.SERVER_INFO = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketStatusOutServerInfo");
        }
    }
}
