package xyz.edge.ac.packetevents.packetwrappers.play.out.kickdisconnect;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketOutKickDisconnect extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> kickDisconnectConstructor;
    private String kickMessage;
    
    public WrappedPacketOutKickDisconnect(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutKickDisconnect(final String kickMessage) {
        this.kickMessage = kickMessage;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutKickDisconnect.kickDisconnectConstructor = PacketTypeClasses.Play.Server.KICK_DISCONNECT.getConstructor(NMSUtils.iChatBaseComponentClass);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public String getKickMessage() {
        if (this.packet != null) {
            final Object iChatBaseComponentObject = this.readObject(0, NMSUtils.iChatBaseComponentClass);
            return NMSUtils.readIChatBaseComponent(iChatBaseComponentObject);
        }
        return this.kickMessage;
    }
    
    public void setKickMessage(final String message) {
        if (this.packet != null) {
            final Object iChatBaseComponent = NMSUtils.generateIChatBaseComponent(message);
            this.write(NMSUtils.iChatBaseComponentClass, 0, iChatBaseComponent);
        }
        else {
            this.kickMessage = message;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutKickDisconnect.kickDisconnectConstructor.newInstance(NMSUtils.generateIChatBaseComponent(NMSUtils.fromStringToJSON(this.getKickMessage())));
    }
}
