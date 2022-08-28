package xyz.edge.ac.packetevents.packetwrappers.login.out.success;

import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.gameprofile.GameProfileUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.gameprofile.WrappedGameProfile;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketLoginOutSuccess extends WrappedPacket
{
    private WrappedGameProfile wrappedGameProfile;
    
    public WrappedPacketLoginOutSuccess(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedGameProfile getGameProfile() {
        if (this.packet != null) {
            return GameProfileUtil.getWrappedGameProfile(this.readObject(0, NMSUtils.gameProfileClass));
        }
        return this.wrappedGameProfile;
    }
    
    public void setGameProfile(final WrappedGameProfile wrappedGameProfile) {
        if (this.packet != null) {
            final Object gameProfile = GameProfileUtil.getGameProfile(wrappedGameProfile.getId(), wrappedGameProfile.getName());
            this.write(NMSUtils.gameProfileClass, 0, gameProfile);
        }
        else {
            this.wrappedGameProfile = wrappedGameProfile;
        }
    }
    
    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.SUCCESS != null;
    }
}
