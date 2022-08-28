package xyz.edge.ac.checks.checks.badpacket;

import xyz.edge.ac.util.utils.PlayerUtil;
import xyz.edge.ac.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "BadPacket (G)", type = "G")
public final class BadPacketG extends EdgeCheck
{
    public BadPacketG(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                final boolean placing = this.user.getPacketActionHandler().isPlacing();
                final boolean sword = PlayerUtil.isHoldingSword(this.user.getPlayer());
                this.debug("Attack actions");
                if (placing && sword) {
                    if (this.increaseBuffer() > 2.0) {
                        this.fail("Attack actions", "P=" + placing + " : S=" + sword + " : WPIUE=A");
                    }
                }
                else {
                    this.decreaseBufferBy(0.5);
                }
            }
        }
    }
}
