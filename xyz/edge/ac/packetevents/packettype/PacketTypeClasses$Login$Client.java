package xyz.edge.ac.packetevents.packettype;

import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;

public static class Client
{
    private static String PREFIX;
    public static Class<?> CUSTOM_PAYLOAD;
    public static Class<?> START;
    public static Class<?> ENCRYPTION_BEGIN;
    
    public static void load() {
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_17)) {
            Client.PREFIX = "net.minecraft.network.protocol.login.";
        }
        else {
            Client.PREFIX = ServerVersion.getNMSDirectory() + ".";
        }
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
            Client.CUSTOM_PAYLOAD = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketLoginInCustomPayload");
        }
        Client.START = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketLoginInStart");
        Client.ENCRYPTION_BEGIN = Reflection.getClassByNameWithoutException(Client.PREFIX + "PacketLoginInEncryptionBegin");
    }
}
