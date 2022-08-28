package xyz.edge.ac.packetevents.packetwrappers.play.out.resourcepacksend;

import java.util.Optional;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutResourcePackSend extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private String url;
    private String hash;
    private boolean forced;
    private String forcedMessage;
    
    public WrappedPacketOutResourcePackSend(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutResourcePackSend(final String url, final String hash) {
        this.url = url;
        this.hash = hash;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutResourcePackSend.v_1_17 = WrappedPacketOutResourcePackSend.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutResourcePackSend.v_1_17) {
                WrappedPacketOutResourcePackSend.packetConstructor = PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND.getConstructor(String.class, String.class, Boolean.TYPE, NMSUtils.iChatBaseComponentClass);
            }
            else {
                WrappedPacketOutResourcePackSend.packetConstructor = PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND.getConstructor(String.class, String.class);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public String getUrl() {
        if (this.packet != null) {
            return this.readString(0);
        }
        return this.url;
    }
    
    public void setUrl(final String url) {
        if (this.packet != null) {
            this.writeString(0, url);
        }
        else {
            this.url = url;
        }
    }
    
    public String getHash() {
        if (this.packet != null) {
            return this.readString(1);
        }
        return this.hash;
    }
    
    public void setHash(final String hash) {
        if (this.packet != null) {
            this.writeString(1, hash);
        }
        else {
            this.hash = hash;
        }
    }
    
    public Optional<Boolean> isForced() {
        if (!WrappedPacketOutResourcePackSend.v_1_17) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readBoolean(0));
        }
        return Optional.of(this.forced);
    }
    
    public void setForced(final boolean forced) {
        if (WrappedPacketOutResourcePackSend.v_1_17) {
            if (this.packet != null) {
                this.writeBoolean(0, forced);
            }
            else {
                this.forced = forced;
            }
        }
    }
    
    public Optional<String> getForcedMessage() {
        if (!WrappedPacketOutResourcePackSend.v_1_17) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readIChatBaseComponent(0));
        }
        return Optional.of(this.forcedMessage);
    }
    
    public void setForcedMessage(final String forcedMessage) {
        if (WrappedPacketOutResourcePackSend.v_1_17) {
            if (this.packet != null) {
                this.writeIChatBaseComponent(0, forcedMessage);
            }
            else {
                this.forcedMessage = forcedMessage;
            }
        }
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutResourcePackSend.version.isNewerThan(ServerVersion.v_1_7_10);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (WrappedPacketOutResourcePackSend.v_1_17) {
            return WrappedPacketOutResourcePackSend.packetConstructor.newInstance(this.getUrl(), this.getHash(), this.isForced().get(), NMSUtils.generateIChatBaseComponent(this.getForcedMessage().get()));
        }
        return WrappedPacketOutResourcePackSend.packetConstructor.newInstance(this.getUrl(), this.getHash());
    }
}
