package xyz.edge.ac.checks.checks.killaura;

import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "KillAura (D)", type = "D")
public final class KillAuraD extends EdgeCheck
{
    public KillAuraD(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                final int targets = this.user.getFightHandler().getCurrentTargets();
                final Entity target = this.user.getFightHandler().getTarget();
                final Entity lastTarget = this.user.getFightHandler().getLastTarget();
                final boolean exempt = target == lastTarget;
                this.debug("Attacked same target in 1 tick");
                this.debug("Attacked multiple targets &8[&cTS=" + targets + "&8]");
                if (!exempt && this.increaseBuffer() > 1.0) {
                    this.fail("Attacked same target in 1 tick", "T=" + target.getName());
                }
                if (targets > 1) {
                    this.fail("Attacked multiple targets", "TS=" + targets);
                }
            }
        }
        else if (packet.PACKET_FLYING()) {
            this.resetBuffer();
        }
    }
}
