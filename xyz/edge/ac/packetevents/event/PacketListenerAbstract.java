package xyz.edge.ac.packetevents.event;

import xyz.edge.ac.packetevents.event.impl.PlayerEjectEvent;
import xyz.edge.ac.packetevents.event.impl.PlayerInjectEvent;
import xyz.edge.ac.packetevents.event.impl.PostPlayerInjectEvent;
import xyz.edge.ac.packetevents.event.impl.PostPacketPlaySendEvent;
import xyz.edge.ac.packetevents.event.impl.PostPacketPlayReceiveEvent;
import xyz.edge.ac.packetevents.event.impl.PacketPlaySendEvent;
import xyz.edge.ac.packetevents.event.impl.PacketPlayReceiveEvent;
import xyz.edge.ac.packetevents.event.impl.PacketLoginSendEvent;
import xyz.edge.ac.packetevents.event.impl.PacketLoginReceiveEvent;
import xyz.edge.ac.packetevents.event.impl.PacketHandshakeReceiveEvent;
import xyz.edge.ac.packetevents.event.impl.PacketStatusSendEvent;
import xyz.edge.ac.packetevents.event.impl.PacketStatusReceiveEvent;
import xyz.edge.ac.packetevents.event.priority.PacketEventPriority;
import xyz.edge.ac.packetevents.utils.immutableset.ImmutableSetCustom;

public abstract class PacketListenerAbstract
{
    private final PacketListenerPriority priority;
    public ImmutableSetCustom<Byte> serverSidedStatusAllowance;
    public ImmutableSetCustom<Byte> serverSidedLoginAllowance;
    public ImmutableSetCustom<Byte> serverSidedPlayAllowance;
    public ImmutableSetCustom<Byte> clientSidedStatusAllowance;
    public ImmutableSetCustom<Byte> clientSidedHandshakeAllowance;
    public ImmutableSetCustom<Byte> clientSidedLoginAllowance;
    public ImmutableSetCustom<Byte> clientSidedPlayAllowance;
    
    @Deprecated
    public PacketListenerAbstract(final PacketEventPriority priority) {
        this(PacketListenerPriority.getById(priority.getPriorityValue()));
    }
    
    public PacketListenerAbstract(final PacketListenerPriority priority) {
        this.priority = priority;
        this.serverSidedStatusAllowance = null;
        this.serverSidedLoginAllowance = null;
        this.serverSidedPlayAllowance = null;
        this.clientSidedStatusAllowance = null;
        this.clientSidedHandshakeAllowance = null;
        this.clientSidedLoginAllowance = null;
        this.clientSidedPlayAllowance = null;
    }
    
    public PacketListenerAbstract() {
        this(PacketListenerPriority.NORMAL);
    }
    
    public PacketListenerPriority getPriority() {
        return this.priority;
    }
    
    public void onPacketStatusReceive(final PacketStatusReceiveEvent event) {
    }
    
    public void onPacketStatusSend(final PacketStatusSendEvent event) {
    }
    
    public void onPacketHandshakeReceive(final PacketHandshakeReceiveEvent event) {
    }
    
    public void onPacketLoginReceive(final PacketLoginReceiveEvent event) {
    }
    
    public void onPacketLoginSend(final PacketLoginSendEvent event) {
    }
    
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
    }
    
    public void onPacketPlaySend(final PacketPlaySendEvent event) {
    }
    
    public void onPostPacketPlayReceive(final PostPacketPlayReceiveEvent event) {
    }
    
    public void onPostPacketPlaySend(final PostPacketPlaySendEvent event) {
    }
    
    public void onPostPlayerInject(final PostPlayerInjectEvent event) {
    }
    
    public void onPlayerInject(final PlayerInjectEvent event) {
    }
    
    public void onPlayerEject(final PlayerEjectEvent event) {
    }
    
    public void onPacketEventExternal(final PacketEvent event) {
    }
    
    public final void addServerSidedStatusFilter(final Byte... statusPacketIDs) {
        if (this.serverSidedStatusAllowance == null) {
            this.serverSidedStatusAllowance = new ImmutableSetCustom<Byte>(statusPacketIDs);
        }
        else {
            this.serverSidedStatusAllowance.addAll(statusPacketIDs);
        }
    }
    
    public final void addServerSidedLoginFilter(final Byte... loginPacketIDs) {
        if (this.serverSidedLoginAllowance == null) {
            this.serverSidedLoginAllowance = new ImmutableSetCustom<Byte>(loginPacketIDs);
        }
        else {
            this.serverSidedLoginAllowance.addAll(loginPacketIDs);
        }
    }
    
    public final void addServerSidedPlayFilter(final Byte... playPacketIDs) {
        if (this.serverSidedPlayAllowance == null) {
            this.serverSidedPlayAllowance = new ImmutableSetCustom<Byte>(playPacketIDs);
        }
        else {
            this.serverSidedPlayAllowance.addAll(playPacketIDs);
        }
    }
    
    public final void addClientSidedStatusFilter(final Byte... statusPacketIDs) {
        if (this.clientSidedStatusAllowance == null) {
            this.clientSidedStatusAllowance = new ImmutableSetCustom<Byte>(statusPacketIDs);
        }
        else {
            this.clientSidedStatusAllowance.addAll(statusPacketIDs);
        }
    }
    
    public final void addClientSidedHandshakeFilter(final Byte... handshakePacketIDs) {
        if (this.clientSidedHandshakeAllowance == null) {
            this.clientSidedHandshakeAllowance = new ImmutableSetCustom<Byte>(handshakePacketIDs);
        }
        else {
            this.clientSidedHandshakeAllowance.addAll(handshakePacketIDs);
        }
    }
    
    public final void addClientSidedLoginFilter(final Byte... loginPacketIDs) {
        if (this.clientSidedLoginAllowance == null) {
            this.clientSidedLoginAllowance = new ImmutableSetCustom<Byte>(loginPacketIDs);
        }
        else {
            this.clientSidedLoginAllowance.addAll(loginPacketIDs);
        }
    }
    
    public final void addClientSidedPlayFilter(final Byte... playPacketIDs) {
        if (this.clientSidedPlayAllowance == null) {
            this.clientSidedPlayAllowance = new ImmutableSetCustom<Byte>(playPacketIDs);
        }
        else {
            this.clientSidedPlayAllowance.addAll(playPacketIDs);
        }
    }
    
    public final void filterAll() {
        this.filterServerSidedStatus();
        this.filterServerSidedLogin();
        this.filterServerSidedPlay();
        this.filterClientSidedStatus();
        this.filterClientSidedHandshake();
        this.filterClientSidedLogin();
        this.filterClientSidedPlay();
    }
    
    public final void filterServerSidedStatus() {
        this.serverSidedStatusAllowance = new ImmutableSetCustom<Byte>();
    }
    
    public final void filterServerSidedLogin() {
        this.serverSidedLoginAllowance = new ImmutableSetCustom<Byte>();
    }
    
    public final void filterServerSidedPlay() {
        this.serverSidedPlayAllowance = new ImmutableSetCustom<Byte>();
    }
    
    public final void filterClientSidedStatus() {
        this.clientSidedStatusAllowance = new ImmutableSetCustom<Byte>();
    }
    
    public final void filterClientSidedHandshake() {
        this.clientSidedHandshakeAllowance = new ImmutableSetCustom<Byte>();
    }
    
    public final void filterClientSidedLogin() {
        this.clientSidedLoginAllowance = new ImmutableSetCustom<Byte>();
    }
    
    public final void filterClientSidedPlay() {
        this.clientSidedPlayAllowance = new ImmutableSetCustom<Byte>();
    }
}
