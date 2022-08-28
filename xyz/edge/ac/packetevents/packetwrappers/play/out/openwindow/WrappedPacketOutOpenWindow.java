package xyz.edge.ac.packetevents.packetwrappers.play.out.openwindow;

import java.util.Optional;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutOpenWindow extends WrappedPacket
{
    private static boolean legacyMode;
    private static boolean ultraLegacyMode;
    private int windowID;
    private int windowTypeID;
    @Deprecated
    private String windowType;
    private String windowTitle;
    
    public WrappedPacketOutOpenWindow(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutOpenWindow.legacyMode = (Reflection.getField(PacketTypeClasses.Play.Server.OPEN_WINDOW, String.class, 0) != null);
        WrappedPacketOutOpenWindow.ultraLegacyMode = (Reflection.getField(PacketTypeClasses.Play.Server.OPEN_WINDOW, Boolean.TYPE, 0) != null);
    }
    
    public int getWindowId() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.windowID;
    }
    
    public void setWindowId(final int windowID) {
        if (this.packet != null) {
            this.writeInt(0, windowID);
        }
        else {
            this.windowID = windowID;
        }
    }
    
    public Optional<Integer> getInventoryTypeId() {
        if (this.packet == null) {
            return Optional.of(this.windowTypeID);
        }
        if (WrappedPacketOutOpenWindow.legacyMode && !WrappedPacketOutOpenWindow.ultraLegacyMode) {
            return Optional.empty();
        }
        return Optional.of(this.readInt(1));
    }
    
    public void setInventoryTypeId(final int inventoryTypeID) {
        if (this.packet != null) {
            if (WrappedPacketOutOpenWindow.legacyMode && !WrappedPacketOutOpenWindow.ultraLegacyMode) {
                return;
            }
            this.writeInt(1, inventoryTypeID);
        }
        else {
            this.windowTypeID = inventoryTypeID;
        }
    }
    
    public Optional<String> getInventoryType() {
        if (this.packet == null) {
            return Optional.of(this.windowType);
        }
        if (!WrappedPacketOutOpenWindow.legacyMode || WrappedPacketOutOpenWindow.ultraLegacyMode) {
            return Optional.empty();
        }
        return Optional.of(this.readString(0));
    }
    
    public String getWindowTitle() {
        if (this.packet == null) {
            return this.windowTitle;
        }
        if (WrappedPacketOutOpenWindow.ultraLegacyMode) {
            return this.readString(0);
        }
        return this.readIChatBaseComponent(0);
    }
    
    public void setWindowTitle(final String title) {
        if (this.packet != null) {
            if (WrappedPacketOutOpenWindow.ultraLegacyMode) {
                this.writeString(0, title);
            }
            else {
                this.writeIChatBaseComponent(0, title);
            }
        }
    }
    
    static {
        WrappedPacketOutOpenWindow.legacyMode = false;
        WrappedPacketOutOpenWindow.ultraLegacyMode = false;
    }
}
