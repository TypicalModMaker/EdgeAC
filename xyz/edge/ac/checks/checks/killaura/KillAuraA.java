package xyz.edge.ac.checks.checks.killaura;

import xyz.edge.ac.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "KillAura (A)", type = "A", exemptBedrock = true)
public class KillAuraA extends EdgeCheck
{
    private long lastFlying;
    private boolean sent;
    
    public KillAuraA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            final long now = System.currentTimeMillis();
            final long delay = now - this.lastFlying;
            this.debug("Attacked on post tick &8[&cD=" + delay + " : S=" + this.sent + "&8]");
            if (this.sent) {
                if (delay > 40L && delay < 100L) {
                    if (this.increaseBuffer() > 1.0) {
                        this.fail("Attacked on post tick", "D=" + delay + " : S=" + this.sent);
                    }
                }
                else {
                    this.decreaseBufferBy(0.125);
                }
                this.sent = false;
            }
            this.lastFlying = now;
        }
        else if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                final long delay2 = System.currentTimeMillis() - this.lastFlying;
                if (delay2 < 10L) {
                    this.sent = true;
                }
            }
        }
    }
}
