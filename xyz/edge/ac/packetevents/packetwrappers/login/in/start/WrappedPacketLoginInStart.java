package xyz.edge.ac.packetevents.packetwrappers.login.in.start;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.gameprofile.GameProfileUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.utils.gameprofile.WrappedGameProfile;
import java.util.Optional;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginInStart extends WrappedPacket
{
    public WrappedPacketLoginInStart(final NMSPacket packet) {
        super(packet);
    }
    
    public Optional<WrappedGameProfile> getGameProfile() {
        if (WrappedPacketLoginInStart.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            return Optional.empty();
        }
        return Optional.of(GameProfileUtil.getWrappedGameProfile(this.readObject(0, NMSUtils.gameProfileClass)));
    }
    
    public void setGameProfile(final WrappedGameProfile wrappedGameProfile) {
        if (WrappedPacketLoginInStart.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            this.writeString(0, wrappedGameProfile.getName());
        }
        else {
            final Object gameProfile = GameProfileUtil.getGameProfile(wrappedGameProfile.getId(), wrappedGameProfile.getName());
            this.write(NMSUtils.gameProfileClass, 0, gameProfile);
        }
    }
    
    public String getUsername() {
        if (WrappedPacketLoginInStart.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            return this.readString(0);
        }
        return GameProfileUtil.getWrappedGameProfile(this.readObject(0, NMSUtils.gameProfileClass)).getName();
    }
    
    public void setUsername(final String username) throws IllegalAccessException {
        if (WrappedPacketLoginInStart.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            this.writeString(0, username);
            return;
        }
        throw new IllegalAccessException("Please use the setGameProfile method in the WrappedPacketLoginInStart wrapper to change the username!");
    }
    
    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Client.START != null;
    }
}
