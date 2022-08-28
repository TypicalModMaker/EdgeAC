package xyz.edge.ac.packetevents.packetwrappers.play.in.settings;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public enum DisplayedSkinPart
{
    CAPE(1), 
    @SupportedVersions(ranges = { ServerVersion.v_1_8, ServerVersion.ERROR })
    JACKET(2), 
    @SupportedVersions(ranges = { ServerVersion.v_1_8, ServerVersion.ERROR })
    LEFT_SLEEVE(4), 
    @SupportedVersions(ranges = { ServerVersion.v_1_8, ServerVersion.ERROR })
    RIGHT_SLEEVE(8), 
    @SupportedVersions(ranges = { ServerVersion.v_1_8, ServerVersion.ERROR })
    LEFT_PANTS(16), 
    @SupportedVersions(ranges = { ServerVersion.v_1_8, ServerVersion.ERROR })
    RIGHT_PANTS(32), 
    @SupportedVersions(ranges = { ServerVersion.v_1_8, ServerVersion.ERROR })
    HAT(64);
    
    final byte maskFlag;
    
    private DisplayedSkinPart(final int maskFlag) {
        this.maskFlag = (byte)maskFlag;
    }
}
