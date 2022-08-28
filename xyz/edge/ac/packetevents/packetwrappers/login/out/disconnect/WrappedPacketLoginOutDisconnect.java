package xyz.edge.ac.packetevents.packetwrappers.login.out.disconnect;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginOutDisconnect extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private String reason;
    
    public WrappedPacketLoginOutDisconnect(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketLoginOutDisconnect(final String reason) {
        this.reason = reason;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketLoginOutDisconnect.packetConstructor = PacketTypeClasses.Login.Server.DISCONNECT.getConstructor(NMSUtils.iChatBaseComponentClass);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public String getReason() {
        if (this.packet != null) {
            return this.readIChatBaseComponent(0);
        }
        return this.reason;
    }
    
    public void setReason(final String reason) {
        if (this.packet != null) {
            this.writeIChatBaseComponent(0, reason);
        }
        else {
            this.reason = reason;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketLoginOutDisconnect.packetConstructor.newInstance(NMSUtils.generateIChatBaseComponent(this.getReason()));
    }
    
    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.DISCONNECT != null;
    }
}
