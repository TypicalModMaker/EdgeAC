package xyz.edge.ac.packetevents.packetwrappers.play.out.transaction;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutTransaction extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private int windowID;
    private short actionNumber;
    private boolean accepted;
    
    public WrappedPacketOutTransaction(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutTransaction(final int windowID, final short actionNumber, final boolean accepted) {
        this.windowID = windowID;
        this.actionNumber = actionNumber;
        this.accepted = accepted;
    }
    
    @Override
    protected void load() {
        final Class<?> packetClass = PacketTypeClasses.Play.Server.TRANSACTION;
        try {
            WrappedPacketOutTransaction.packetConstructor = packetClass.getConstructor(Integer.TYPE, Short.TYPE, Boolean.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
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
    
    public short getActionNumber() {
        if (this.packet != null) {
            return this.readShort(0);
        }
        return this.actionNumber;
    }
    
    public void setActionNumber(final short actionNumber) {
        if (this.packet != null) {
            this.writeShort(0, actionNumber);
        }
        else {
            this.actionNumber = actionNumber;
        }
    }
    
    public boolean isAccepted() {
        if (this.packet != null) {
            return this.readBoolean(0);
        }
        return this.accepted;
    }
    
    public void setAccepted(final boolean isAccepted) {
        if (this.packet != null) {
            this.writeBoolean(0, isAccepted);
        }
        else {
            this.accepted = isAccepted;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutTransaction.packetConstructor.newInstance(this.getWindowId(), this.getActionNumber(), this.isAccepted());
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutTransaction.version.isOlderThan(ServerVersion.v_1_17);
    }
}
