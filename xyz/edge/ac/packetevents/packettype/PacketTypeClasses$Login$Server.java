package xyz.edge.ac.packetevents.packettype;

import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public static class Server
{
    private static String PREFIX;
    public static Class<?> CUSTOM_PAYLOAD;
    public static Class<?> DISCONNECT;
    public static Class<?> ENCRYPTION_BEGIN;
    public static Class<?> SUCCESS;
    public static Class<?> SET_COMPRESSION;
    
    public static void load() {
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
            Server.PREFIX = "net.minecraft.network.protocol.login.";
        }
        else {
            Server.PREFIX = ServerVersion.getNMSDirectory() + ".";
        }
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
            Server.CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutCustomPayload");
        }
        Server.DISCONNECT = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutDisconnect");
        Server.ENCRYPTION_BEGIN = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutEncryptionBegin");
        Server.SUCCESS = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutSuccess");
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_7_10)) {
            Server.SET_COMPRESSION = Reflection.getClassByNameWithoutException(Server.PREFIX + "PacketLoginOutSetCompression");
        }
    }
}
