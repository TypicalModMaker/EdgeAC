package xyz.edge.ac.packetevents.processor;

import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.utils.reflection.ClassUtil;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.packetevents.packetwrappers.handshaking.setprotocol.WrappedPacketHandshakingInSetProtocol;
import java.util.UUID;
import xyz.edge.ac.packetevents.event.impl.PostPacketPlaySendEvent;
import xyz.edge.ac.packetevents.event.impl.PostPacketPlayReceiveEvent;
import xyz.edge.ac.packetevents.event.impl.PacketPlaySendEvent;
import xyz.edge.ac.packetevents.event.impl.PacketLoginSendEvent;
import xyz.edge.ac.packetevents.event.impl.PacketStatusSendEvent;
import xyz.edge.ac.packetevents.packettype.PacketState;
import xyz.edge.ac.packetevents.event.impl.PacketPlayReceiveEvent;
import xyz.edge.ac.packetevents.packetwrappers.login.in.start.WrappedPacketLoginInStart;
import xyz.edge.ac.packetevents.event.impl.PacketLoginReceiveEvent;
import xyz.edge.ac.packetevents.event.impl.PacketHandshakeReceiveEvent;
import xyz.edge.ac.packetevents.event.PacketEvent;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.event.impl.PacketStatusReceiveEvent;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.entity.Player;

public class PacketProcessorInternal
{
    public PacketData read(final Player player, final Object channel, Object packet) {
        final PacketData data = new PacketData();
        data.packet = packet;
        final PacketState state = this.getPacketState(player, packet);
        if (state == null) {
            return data;
        }
        switch (state) {
            case STATUS: {
                final PacketStatusReceiveEvent statusEvent = new PacketStatusReceiveEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(statusEvent);
                packet = statusEvent.getNMSPacket().getRawNMSPacket();
                this.interceptStatusReceive(statusEvent);
                if (statusEvent.isCancelled()) {
                    packet = null;
                    break;
                }
                break;
            }
            case HANDSHAKING: {
                final PacketHandshakeReceiveEvent handshakeEvent = new PacketHandshakeReceiveEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(handshakeEvent);
                packet = handshakeEvent.getNMSPacket().getRawNMSPacket();
                this.interceptHandshakeReceive(handshakeEvent);
                if (handshakeEvent.isCancelled()) {
                    packet = null;
                    break;
                }
                break;
            }
            case LOGIN: {
                final PacketLoginReceiveEvent loginEvent = new PacketLoginReceiveEvent(channel, new NMSPacket(packet));
                if (loginEvent.getPacketId() == -121) {
                    final WrappedPacketLoginInStart startWrapper = new WrappedPacketLoginInStart(loginEvent.getNMSPacket());
                    final String username = startWrapper.getUsername();
                    PacketEvents.get().getPlayerUtils().channels.put(username, channel);
                }
                PacketEvents.get().getEventManager().callEvent(loginEvent);
                packet = loginEvent.getNMSPacket().getRawNMSPacket();
                this.interceptLoginReceive(loginEvent);
                if (loginEvent.isCancelled()) {
                    packet = null;
                    break;
                }
                break;
            }
            case PLAY: {
                final PacketPlayReceiveEvent event = new PacketPlayReceiveEvent(player, channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(event);
                packet = event.getNMSPacket().getRawNMSPacket();
                this.interceptPlayReceive(event);
                if (event.isCancelled()) {
                    packet = null;
                    break;
                }
                break;
            }
        }
        data.packet = packet;
        return data;
    }
    
    public PacketData write(final Player player, final Object channel, Object packet) {
        final PacketData data = new PacketData();
        data.packet = packet;
        final PacketState state = this.getPacketState(player, packet);
        if (state == null) {
            return data;
        }
        switch (state) {
            case STATUS: {
                final PacketStatusSendEvent statusEvent = new PacketStatusSendEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(statusEvent);
                if (statusEvent.isPostTaskAvailable()) {
                    data.postAction = statusEvent.getPostTask();
                }
                packet = statusEvent.getNMSPacket().getRawNMSPacket();
                this.interceptStatusSend(statusEvent);
                if (statusEvent.isCancelled()) {
                    packet = null;
                    break;
                }
                break;
            }
            case LOGIN: {
                final PacketLoginSendEvent loginEvent = new PacketLoginSendEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(loginEvent);
                if (loginEvent.isPostTaskAvailable()) {
                    data.postAction = loginEvent.getPostTask();
                }
                packet = loginEvent.getNMSPacket().getRawNMSPacket();
                this.interceptLoginSend(loginEvent);
                if (loginEvent.isCancelled()) {
                    packet = null;
                    break;
                }
                break;
            }
            case PLAY: {
                final PacketPlaySendEvent playEvent = new PacketPlaySendEvent(player, channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(playEvent);
                if (playEvent.isPostTaskAvailable()) {
                    data.postAction = playEvent.getPostTask();
                }
                packet = playEvent.getNMSPacket().getRawNMSPacket();
                this.interceptPlaySend(playEvent);
                if (playEvent.isCancelled()) {
                    packet = null;
                    break;
                }
                break;
            }
        }
        data.packet = packet;
        return data;
    }
    
    public void postRead(final Player player, final Object channel, final Object packet) {
        if (this.getPacketState(player, packet) == PacketState.PLAY) {
            final PostPacketPlayReceiveEvent event = new PostPacketPlayReceiveEvent(player, channel, new NMSPacket(packet));
            PacketEvents.get().getEventManager().callEvent(event);
            this.interceptPostPlayReceive(event);
        }
    }
    
    public void postWrite(final Player player, final Object channel, final Object packet) {
        if (this.getPacketState(player, packet) == PacketState.PLAY) {
            final PostPacketPlaySendEvent event = new PostPacketPlaySendEvent(player, channel, new NMSPacket(packet));
            PacketEvents.get().getEventManager().callEvent(event);
            this.interceptPostPlaySend(event);
        }
    }
    
    private void interceptPlayReceive(final PacketPlayReceiveEvent event) {
        if (event.getPacketId() == -98) {
            final UUID uuid = event.getPlayer().getUniqueId();
            final long timestamp = PacketEvents.get().getPlayerUtils().keepAliveMap.getOrDefault(uuid, event.getTimestamp());
            final long currentTime = event.getTimestamp();
            final long ping = currentTime - timestamp;
            final long smoothedPing = (PacketEvents.get().getPlayerUtils().getSmoothedPing(event.getPlayer().getUniqueId()) * 3L + ping) / 4L;
            PacketEvents.get().getPlayerUtils().playerPingMap.put(uuid, (int)ping);
            PacketEvents.get().getPlayerUtils().playerSmoothedPingMap.put(uuid, (int)smoothedPing);
        }
    }
    
    private void interceptPlaySend(final PacketPlaySendEvent event) {
    }
    
    private void interceptLoginReceive(final PacketLoginReceiveEvent event) {
    }
    
    private void interceptLoginSend(final PacketLoginSendEvent event) {
    }
    
    private void interceptHandshakeReceive(final PacketHandshakeReceiveEvent event) {
        if (event.getPacketId() == -123) {
            final WrappedPacketHandshakingInSetProtocol handshake = new WrappedPacketHandshakingInSetProtocol(event.getNMSPacket());
            final int protocolVersion = handshake.getProtocolVersion();
            final ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
            PacketEvents.get().getPlayerUtils().tempClientVersionMap.put(event.getSocketAddress(), version);
        }
    }
    
    private void interceptStatusReceive(final PacketStatusReceiveEvent event) {
    }
    
    private void interceptStatusSend(final PacketStatusSendEvent event) {
    }
    
    private void interceptPostPlayReceive(final PostPacketPlayReceiveEvent event) {
    }
    
    private void interceptPostPlaySend(final PostPacketPlaySendEvent event) {
        if (event.getPacketId() == -34 && event.getPlayer() != null) {
            PacketEvents.get().getPlayerUtils().keepAliveMap.put(event.getPlayer().getUniqueId(), event.getTimestamp());
        }
    }
    
    @Nullable
    private PacketState getPacketState(final Player player, final Object packet) {
        if (packet == null) {
            return null;
        }
        if (player != null) {
            return PacketState.PLAY;
        }
        final String packetName = ClassUtil.getClassSimpleName(packet.getClass());
        if (packetName.startsWith("PacketH")) {
            return PacketState.HANDSHAKING;
        }
        if (packetName.startsWith("PacketL")) {
            return PacketState.LOGIN;
        }
        if (packetName.startsWith("PacketS")) {
            return PacketState.STATUS;
        }
        return null;
    }
    
    public class PacketData
    {
        public Object packet;
        public Runnable postAction;
    }
}
