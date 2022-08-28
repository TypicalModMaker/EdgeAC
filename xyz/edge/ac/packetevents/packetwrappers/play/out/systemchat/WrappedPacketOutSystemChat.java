package xyz.edge.ac.packetevents.packetwrappers.play.out.systemchat;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.play.out.chat.WrappedPacketOutChat;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutSystemChat extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private String message;
    private WrappedPacketOutChat.ChatPosition position;
    
    public WrappedPacketOutSystemChat(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutSystemChat(final String messageJSON, final WrappedPacketOutChat.ChatPosition position) {
        this.message = messageJSON;
        this.position = position;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutSystemChat.packetConstructor = PacketTypeClasses.Play.Server.SYSTEM_CHAT.getConstructor(String.class, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public String getMessageJSON() {
        if (this.packet != null) {
            return this.readString(0);
        }
        return this.message;
    }
    
    public void setMessageJSON(final String message) {
        if (this.packet != null) {
            this.writeString(0, message);
        }
        else {
            this.message = message;
        }
    }
    
    public WrappedPacketOutChat.ChatPosition getPosition() {
        if (this.packet != null) {
            return WrappedPacketOutChat.ChatPosition.values()[this.readInt(0)];
        }
        return this.position;
    }
    
    public void setPosition(final WrappedPacketOutChat.ChatPosition position) {
        if (this.packet != null) {
            this.writeInt(0, position.ordinal());
        }
        else {
            this.position = position;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutSystemChat.packetConstructor.newInstance(this.getMessageJSON(), this.getPosition().ordinal());
    }
}
