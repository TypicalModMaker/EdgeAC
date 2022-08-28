package xyz.edge.ac.packetevents.utils.player;

import xyz.edge.ac.packetevents.utils.geyser.GeyserUtils;
import xyz.edge.ac.packetevents.utils.gameprofile.GameProfileUtil;
import xyz.edge.ac.packetevents.utils.gameprofile.WrappedGameProfile;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.utils.versionlookup.v_1_7_10.SpigotVersionLookup_1_7;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.versionlookup.VersionLookupUtils;
import org.jetbrains.annotations.NotNull;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import org.bukkit.entity.Player;
import java.util.concurrent.ConcurrentHashMap;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.Map;

public final class PlayerUtils
{
    public final Map<UUID, Long> loginTime;
    public final Map<UUID, Integer> playerPingMap;
    public final Map<UUID, Integer> playerSmoothedPingMap;
    public final Map<InetSocketAddress, ClientVersion> clientVersionsMap;
    public final Map<UUID, Long> keepAliveMap;
    public final Map<String, Object> channels;
    public final Map<InetSocketAddress, ClientVersion> tempClientVersionMap;
    
    public PlayerUtils() {
        this.loginTime = new ConcurrentHashMap<UUID, Long>();
        this.playerPingMap = new ConcurrentHashMap<UUID, Integer>();
        this.playerSmoothedPingMap = new ConcurrentHashMap<UUID, Integer>();
        this.clientVersionsMap = new ConcurrentHashMap<InetSocketAddress, ClientVersion>();
        this.keepAliveMap = new ConcurrentHashMap<UUID, Long>();
        this.channels = new ConcurrentHashMap<String, Object>();
        this.tempClientVersionMap = new ConcurrentHashMap<InetSocketAddress, ClientVersion>();
    }
    
    @Deprecated
    public int getNMSPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }
    
    public int getPing(final Player player) {
        return this.getPing(player.getUniqueId());
    }
    
    @Deprecated
    public int getSmoothedPing(final Player player) {
        return this.getSmoothedPing(player.getUniqueId());
    }
    
    @Deprecated
    public int getPing(final UUID uuid) {
        final Integer ping = this.playerPingMap.get(uuid);
        if (ping != null) {
            return ping;
        }
        final Long joinTime = this.loginTime.get(uuid);
        if (joinTime == null) {
            return 0;
        }
        return (int)(System.currentTimeMillis() - joinTime);
    }
    
    @Deprecated
    public int getSmoothedPing(final UUID uuid) {
        final Integer smoothedPing = this.playerSmoothedPingMap.get(uuid);
        if (smoothedPing != null) {
            return smoothedPing;
        }
        final Long joinTime = this.loginTime.get(uuid);
        if (joinTime == null) {
            return 0;
        }
        return (int)(System.currentTimeMillis() - joinTime);
    }
    
    @NotNull
    public ClientVersion getClientVersion(@NotNull final Player player) {
        if (player.getAddress() == null) {
            return ClientVersion.UNKNOWN;
        }
        ClientVersion version = this.clientVersionsMap.get(player.getAddress());
        if (version == null || !version.isResolved()) {
            if (VersionLookupUtils.isDependencyAvailable()) {
                try {
                    version = ClientVersion.getClientVersion(VersionLookupUtils.getProtocolVersion(player));
                    this.clientVersionsMap.put(player.getAddress(), version);
                    return version;
                }
                catch (final Exception ex) {
                    return ClientVersion.TEMP_UNRESOLVED;
                }
            }
            version = this.tempClientVersionMap.get(player.getAddress());
            if (version == null) {
                int protocolVersion;
                if (PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_8)) {
                    protocolVersion = SpigotVersionLookup_1_7.getProtocolVersion(player);
                }
                else {
                    protocolVersion = PacketEvents.get().getServerUtils().getVersion().getProtocolVersion();
                }
                version = ClientVersion.getClientVersion(protocolVersion);
            }
            this.clientVersionsMap.put(player.getAddress(), version);
        }
        return version;
    }
    
    public void writePacket(final Player player, final SendableWrapper wrapper) {
        try {
            final Object nmsPacket = wrapper.asNMSPacket();
            PacketEvents.get().getInjector().writePacket(this.getChannel(player), nmsPacket);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void flushPackets(final Player player) {
        try {
            PacketEvents.get().getInjector().flushPackets(this.getChannel(player));
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void sendPacket(final Player player, final SendableWrapper wrapper) {
        try {
            final Object nmsPacket = wrapper.asNMSPacket();
            PacketEvents.get().getInjector().sendPacket(this.getChannel(player), nmsPacket);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Deprecated
    public void sendPacket(final Object channel, final SendableWrapper wrapper) {
        try {
            final Object nmsPacket = wrapper.asNMSPacket();
            PacketEvents.get().getInjector().sendPacket(channel, nmsPacket);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void sendNMSPacket(final Player player, final Object packet) {
        PacketEvents.get().getInjector().sendPacket(this.getChannel(player), packet);
    }
    
    @Deprecated
    public void sendNMSPacket(final Object channel, final Object packet) {
        PacketEvents.get().getInjector().sendPacket(channel, packet);
    }
    
    public WrappedGameProfile getGameProfile(final Player player) {
        final Object gameProfile = GameProfileUtil.getGameProfile(player.getUniqueId(), player.getName());
        return GameProfileUtil.getWrappedGameProfile(gameProfile);
    }
    
    public boolean isGeyserPlayer(final Player player) {
        return PacketEvents.get().getServerUtils().isGeyserAvailable() && GeyserUtils.isGeyserPlayer(player.getUniqueId());
    }
    
    public boolean isGeyserPlayer(final UUID uuid) {
        return PacketEvents.get().getServerUtils().isGeyserAvailable() && GeyserUtils.isGeyserPlayer(uuid);
    }
    
    public void changeSkinProperty(final Player player, final Skin skin) {
        final Object gameProfile = NMSUtils.getGameProfile(player);
        GameProfileUtil.setGameProfileSkin(gameProfile, skin);
    }
    
    public Skin getSkin(final Player player) {
        final Object gameProfile = NMSUtils.getGameProfile(player);
        return GameProfileUtil.getGameProfileSkin(gameProfile);
    }
    
    public Object getChannel(final Player player) {
        final String name = player.getName();
        Object channel = this.channels.get(name);
        if (channel == null) {
            channel = NMSUtils.getChannel(player);
            if (channel != null) {
                this.channels.put(name, channel);
            }
        }
        return channel;
    }
}
