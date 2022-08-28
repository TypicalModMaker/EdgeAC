package xyz.edge.ac.packetevents.utils.server;

import xyz.edge.ac.packetevents.PacketEvents;
import org.bukkit.Bukkit;

public enum ServerVersion
{
    v_1_7_10(5), 
    v_1_8(47), 
    v_1_8_3(47), 
    v_1_8_8(47), 
    v_1_9(107), 
    v_1_9_2(109), 
    v_1_9_4(110), 
    v_1_10(210), 
    v_1_10_1(210), 
    v_1_10_2(210), 
    v_1_11(315), 
    v_1_11_2(316), 
    v_1_12(335), 
    v_1_12_1(338), 
    v_1_12_2(340), 
    v_1_13(393), 
    v_1_13_1(401), 
    v_1_13_2(404), 
    v_1_14(477), 
    v_1_14_1(480), 
    v_1_14_2(485), 
    v_1_14_3(490), 
    v_1_14_4(498), 
    v_1_15(573), 
    v_1_15_1(575), 
    v_1_15_2(578), 
    v_1_16(735), 
    v_1_16_1(736), 
    v_1_16_2(751), 
    v_1_16_3(753), 
    v_1_16_4(754), 
    v_1_16_5(754), 
    v_1_17(755), 
    v_1_17_1(756), 
    v_1_18(757), 
    v_1_18_1(757), 
    v_1_18_2(758), 
    v_1_19(759), 
    ERROR(-1);
    
    private static final String NMS_VERSION_SUFFIX;
    private static final ServerVersion[] VALUES;
    public static ServerVersion[] reversedValues;
    private static ServerVersion cachedVersion;
    private final int protocolVersion;
    
    private ServerVersion(final int protocolId) {
        this.protocolVersion = protocolId;
    }
    
    private static ServerVersion getVersionNoCache() {
        if (ServerVersion.reversedValues[0] == null) {
            ServerVersion.reversedValues = reverse();
        }
        for (final ServerVersion val : ServerVersion.reversedValues) {
            final String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            }
        }
        ServerVersion fallbackVersion = PacketEvents.get().getSettings().getFallbackServerVersion();
        if (fallbackVersion != null) {
            if (fallbackVersion == ServerVersion.v_1_7_10) {
                try {
                    Class.forName("net.minecraft.util.io.netty.buffer.ByteBuf");
                }
                catch (final Exception ex) {
                    fallbackVersion = ServerVersion.v_1_8_8;
                }
            }
            PacketEvents.get().getPlugin().getLogger().warning("[packetevents] Your server software is preventing us from checking the server version. This is what we found: " + Bukkit.getBukkitVersion() + ". We will assume the server version is " + fallbackVersion.name() + "...");
            return fallbackVersion;
        }
        return ServerVersion.ERROR;
    }
    
    public static ServerVersion getVersion() {
        if (ServerVersion.cachedVersion == null) {
            ServerVersion.cachedVersion = getVersionNoCache();
        }
        return ServerVersion.cachedVersion;
    }
    
    private static ServerVersion[] reverse() {
        final ServerVersion[] array = values();
        ServerVersion tmp;
        for (int i = 0, j = array.length - 1; j > i; array[j--] = array[i], array[i++] = tmp) {
            tmp = array[j];
        }
        return array;
    }
    
    public static String getNMSSuffix() {
        return ServerVersion.NMS_VERSION_SUFFIX;
    }
    
    public static String getNMSDirectory() {
        return "net.minecraft.server." + getNMSSuffix();
    }
    
    public static String getOBCDirectory() {
        return "org.bukkit.craftbukkit." + getNMSSuffix();
    }
    
    public static ServerVersion getLatest() {
        return ServerVersion.reversedValues[1];
    }
    
    public static ServerVersion getOldest() {
        return values()[0];
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
    
    public boolean isNewerThan(final ServerVersion target) {
        if (target.protocolVersion != this.protocolVersion || this == target) {
            return this.protocolVersion > target.protocolVersion;
        }
        for (final ServerVersion version : ServerVersion.reversedValues) {
            if (version == target) {
                return false;
            }
            if (version == this) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isOlderThan(final ServerVersion target) {
        if (target.protocolVersion != this.protocolVersion || this == target) {
            return this.protocolVersion < target.protocolVersion;
        }
        for (final ServerVersion version : ServerVersion.VALUES) {
            if (version == this) {
                return true;
            }
            if (version == target) {
                return false;
            }
        }
        return false;
    }
    
    public boolean isNewerThanOrEquals(final ServerVersion target) {
        return this == target || this.isNewerThan(target);
    }
    
    public boolean isOlderThanOrEquals(final ServerVersion target) {
        return this == target || this.isOlderThan(target);
    }
    
    @Deprecated
    public boolean isHigherThan(final ServerVersion target) {
        return this.isNewerThan(target);
    }
    
    @Deprecated
    public boolean isHigherThanOrEquals(final ServerVersion target) {
        return this.isNewerThanOrEquals(target);
    }
    
    @Deprecated
    public boolean isLowerThan(final ServerVersion target) {
        return this.isOlderThan(target);
    }
    
    @Deprecated
    public boolean isLowerThanOrEquals(final ServerVersion target) {
        return this.isOlderThanOrEquals(target);
    }
    
    static {
        NMS_VERSION_SUFFIX = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        VALUES = values();
        ServerVersion.reversedValues = new ServerVersion[ServerVersion.VALUES.length];
    }
}
