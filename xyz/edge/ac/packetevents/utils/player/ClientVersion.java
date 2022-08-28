package xyz.edge.ac.packetevents.utils.player;

import java.util.IdentityHashMap;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public enum ClientVersion
{
    v_1_7_10(5), 
    v_1_8(47), 
    v_1_9(107), 
    v_1_9_1(108), 
    v_1_9_2(109), 
    v_1_9_3(110), 
    v_1_10(210), 
    v_1_11(315), 
    v_1_11_1(316), 
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
    v_1_17(755), 
    v_1_17_1(756), 
    v_1_18(757), 
    v_1_18_2(758), 
    v_1_19(759), 
    LOWER_THAN_SUPPORTED_VERSIONS(ClientVersion.v_1_7_10.protocolVersion - 1), 
    HIGHER_THAN_SUPPORTED_VERSIONS(ClientVersion.v_1_19.protocolVersion + 1), 
    ANY_PRE_RELEASE_VERSION(0), 
    TEMP_UNRESOLVED(-1), 
    @Deprecated
    UNRESOLVED(-1), 
    UNKNOWN(-1);
    
    private static final int LOWEST_SUPPORTED_PROTOCOL_VERSION;
    private static final int HIGHEST_SUPPORTED_PROTOCOL_VERSION;
    private static final Map<Integer, ClientVersion> CLIENT_VERSION_CACHE;
    private static final int[] CLIENT_VERSIONS;
    private int protocolVersion;
    
    private ClientVersion(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    
    @NotNull
    public static ClientVersion getClientVersion(final int protocolVersion) {
        if (protocolVersion == -1) {
            return ClientVersion.UNRESOLVED;
        }
        if (protocolVersion < ClientVersion.LOWEST_SUPPORTED_PROTOCOL_VERSION) {
            return ClientVersion.LOWER_THAN_SUPPORTED_VERSIONS;
        }
        if (protocolVersion > ClientVersion.HIGHEST_SUPPORTED_PROTOCOL_VERSION) {
            return ClientVersion.HIGHER_THAN_SUPPORTED_VERSIONS;
        }
        ClientVersion cached = ClientVersion.CLIENT_VERSION_CACHE.get(protocolVersion);
        if (cached == null) {
            for (final ClientVersion version : values()) {
                if (version.protocolVersion > protocolVersion) {
                    break;
                }
                if (version.protocolVersion == protocolVersion) {
                    ClientVersion.CLIENT_VERSION_CACHE.put(protocolVersion, version);
                    return version;
                }
            }
            cached = ClientVersion.UNKNOWN;
            cached.protocolVersion = protocolVersion;
        }
        return cached;
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
    
    public boolean isNewerThan(final ClientVersion target) {
        return this.protocolVersion > target.protocolVersion && target != ClientVersion.UNRESOLVED && this != ClientVersion.UNRESOLVED && target != ClientVersion.TEMP_UNRESOLVED && this != ClientVersion.TEMP_UNRESOLVED;
    }
    
    public boolean isNewerThanOrEquals(final ClientVersion target) {
        return this == target || this.isNewerThan(target);
    }
    
    public boolean isOlderThan(final ClientVersion target) {
        return this.protocolVersion < target.protocolVersion && target != ClientVersion.UNRESOLVED && this != ClientVersion.UNRESOLVED && target != ClientVersion.TEMP_UNRESOLVED && this != ClientVersion.TEMP_UNRESOLVED;
    }
    
    public boolean isOlderThanOrEquals(final ClientVersion target) {
        return this == target || this.isOlderThan(target);
    }
    
    @Deprecated
    public boolean isHigherThan(final ClientVersion target) {
        return this.isNewerThan(target);
    }
    
    @Deprecated
    public boolean isHigherThanOrEquals(final ClientVersion target) {
        return this.isNewerThanOrEquals(target);
    }
    
    @Deprecated
    public boolean isLowerThan(final ClientVersion target) {
        return this.isOlderThan(target);
    }
    
    @Deprecated
    public boolean isLowerThanOrEquals(final ClientVersion target) {
        return this.isOlderThanOrEquals(target);
    }
    
    public boolean isPreRelease() {
        return this.protocolVersion <= ClientVersion.LOWEST_SUPPORTED_PROTOCOL_VERSION || this.protocolVersion >= ClientVersion.HIGHEST_SUPPORTED_PROTOCOL_VERSION || Arrays.binarySearch(ClientVersion.CLIENT_VERSIONS, this.protocolVersion) < 0;
    }
    
    public boolean isResolved() {
        return this != ClientVersion.TEMP_UNRESOLVED && this != ClientVersion.UNRESOLVED;
    }
    
    static {
        LOWEST_SUPPORTED_PROTOCOL_VERSION = ClientVersion.LOWER_THAN_SUPPORTED_VERSIONS.protocolVersion + 1;
        HIGHEST_SUPPORTED_PROTOCOL_VERSION = ClientVersion.HIGHER_THAN_SUPPORTED_VERSIONS.protocolVersion - 1;
        CLIENT_VERSION_CACHE = new IdentityHashMap<Integer, ClientVersion>();
        CLIENT_VERSIONS = new int[] { 5, 47, 107, 108, 109, 110, 210, 315, 316, 335, 338, 340, 393, 401, 404, 477, 480, 485, 490, 498, 573, 575, 578, 735, 736, 751, 753, 754, 755, 756, 757, 758, 759 };
    }
}
