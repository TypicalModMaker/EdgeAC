package xyz.edge.ac.packetevents.packetwrappers.play.in.settings;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInSettings extends WrappedPacket
{
    private static Class<? extends Enum<?>> chatVisibilityEnumClass;
    private static boolean isLowerThan_v_1_8;
    private static boolean v_1_17;
    private static boolean v_1_18;
    
    public WrappedPacketInSettings(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInSettings.isLowerThan_v_1_8 = WrappedPacketInSettings.version.isOlderThan(ServerVersion.v_1_8);
        WrappedPacketInSettings.v_1_17 = WrappedPacketInSettings.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketInSettings.v_1_18 = WrappedPacketInSettings.version.isNewerThanOrEquals(ServerVersion.v_1_18);
        WrappedPacketInSettings.chatVisibilityEnumClass = NMSUtils.getNMSEnumClassWithoutException("EnumChatVisibility");
        if (WrappedPacketInSettings.chatVisibilityEnumClass == null) {
            WrappedPacketInSettings.chatVisibilityEnumClass = NMSUtils.getNMEnumClassWithoutException("world.entity.player.EnumChatVisibility");
            if (WrappedPacketInSettings.chatVisibilityEnumClass == null) {
                WrappedPacketInSettings.chatVisibilityEnumClass = SubclassUtil.getEnumSubClass(NMSUtils.entityHumanClass, "EnumChatVisibility");
            }
        }
    }
    
    public String getLocale() {
        return this.readString(0);
    }
    
    public void setLocale(final String locale) {
        this.writeString(0, locale);
    }
    
    public int getViewDistance() {
        return this.readInt((WrappedPacketInSettings.v_1_17 && !WrappedPacketInSettings.v_1_18) ? 1 : 0);
    }
    
    public void setViewDistance(final int viewDistance) {
        this.writeInt((WrappedPacketInSettings.v_1_17 && !WrappedPacketInSettings.v_1_18) ? 1 : 0, viewDistance);
    }
    
    public ChatVisibility getChatVisibility() {
        final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketInSettings.chatVisibilityEnumClass);
        return ChatVisibility.values()[enumConst.ordinal()];
    }
    
    public void setChatVisibility(final ChatVisibility visibility) {
        final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketInSettings.chatVisibilityEnumClass, visibility.ordinal());
        this.writeEnumConstant(0, enumConst);
    }
    
    public boolean isChatColored() {
        return this.readBoolean(0);
    }
    
    public void setChatColored(final boolean chatColors) {
        this.writeBoolean(0, chatColors);
    }
    
    public boolean isTextFilteringEnabled() {
        return WrappedPacketInSettings.v_1_17 && this.readBoolean(1);
    }
    
    public void setTextFilteringEnabled(final boolean enabled) {
        if (WrappedPacketInSettings.v_1_17) {
            this.writeBoolean(1, enabled);
        }
    }
    
    public boolean isServerListingsAllowed() {
        return !WrappedPacketInSettings.v_1_18 || this.readBoolean(2);
    }
    
    public void setServerListingsAllowed(final boolean allowed) {
        if (WrappedPacketInSettings.v_1_18) {
            this.writeBoolean(2, allowed);
        }
    }
    
    public byte getDisplaySkinPartsMask() {
        byte mask = 0;
        if (WrappedPacketInSettings.isLowerThan_v_1_8) {
            final boolean capeEnabled = this.readBoolean(1);
            if (capeEnabled) {
                mask |= 0x1;
            }
        }
        else {
            mask = (byte)this.readInt((WrappedPacketInSettings.v_1_17 && !WrappedPacketInSettings.v_1_18) ? 2 : 1);
        }
        return mask;
    }
    
    public void setDisplaySkinPartsMask(final byte mask) {
        if (WrappedPacketInSettings.isLowerThan_v_1_8) {
            final boolean capeEnabled = (mask & 0x1) == 0x1;
            this.writeBoolean(1, capeEnabled);
        }
        else {
            this.writeInt((WrappedPacketInSettings.v_1_17 && !WrappedPacketInSettings.v_1_18) ? 2 : 1, mask);
        }
    }
    
    public Set<DisplayedSkinPart> getDisplayedSkinParts() {
        final Set<DisplayedSkinPart> displayedSkinParts = new HashSet<DisplayedSkinPart>();
        final byte mask = this.getDisplaySkinPartsMask();
        for (final DisplayedSkinPart part : DisplayedSkinPart.values()) {
            if ((mask & part.maskFlag) == part.maskFlag) {
                displayedSkinParts.add(part);
            }
        }
        return displayedSkinParts;
    }
    
    public void setDisplayedSkinParts(final Set<DisplayedSkinPart> displayedSkinParts) {
        byte mask = 0;
        for (final DisplayedSkinPart part : displayedSkinParts) {
            mask |= part.maskFlag;
        }
        this.setDisplaySkinPartsMask(mask);
    }
    
    public enum ChatVisibility
    {
        FULL, 
        SYSTEM, 
        HIDDEN;
    }
    
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
}
