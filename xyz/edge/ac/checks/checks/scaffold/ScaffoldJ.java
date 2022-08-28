package xyz.edge.ac.checks.checks.scaffold;

import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (J)", type = "J")
public class ScaffoldJ extends EdgeCheck
{
    private int ticks;
    private int stage;
    private int count;
    private long lastpacketDelta;
    private double fastBreakBuffer;
    
    public ScaffoldJ(final User user) {
        super(user);
        this.fastBreakBuffer = 0.0;
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            if (this.stage == 2) {
                this.fastBreakBuffer -= Math.min(this.getBuffer() + 1.0, 0.01);
            }
            if (this.stage == 1) {
                ++this.ticks;
                this.stage = 2;
            }
            else {
                this.stage = 0;
            }
        }
        if (packet.PACKET_ARM_ANIMATION()) {
            this.setBuffer(0.0);
        }
        else if (packet.PACKET_BLOCK_DIG()) {
            final WrappedPacketInBlockDig wrapped = new WrappedPacketInBlockDig(packet.getRawPacket());
            ++this.count;
            final long packetDelta = System.currentTimeMillis() - this.lastpacketDelta;
            if (packetDelta >= 1000L) {
                this.lastpacketDelta = System.currentTimeMillis();
                if (this.count >= 50) {
                    this.fail("Tried to break blocks incorrectly (Nuker Creative)", "C=" + this.count);
                }
                this.count = 0;
            }
            if (wrapped.getDigType() == WrappedPacketInBlockDig.PlayerDigType.STOP_DESTROY_BLOCK) {
                this.stage = 1;
                this.fastBreakBuffer -= 1.0E-4;
            }
            if (wrapped.getDigType() == WrappedPacketInBlockDig.PlayerDigType.START_DESTROY_BLOCK) {
                if (this.stage == 2 && (this.ticks != 1 || this.user.getVersion().isOlderThan(ClientVersion.v_1_9))) {
                    this.fail("Tried to break block too fast", "S=" + this.stage);
                }
                this.stage = 0;
                this.ticks = 0;
            }
            if (wrapped.getDigType() == WrappedPacketInBlockDig.PlayerDigType.START_DESTROY_BLOCK || wrapped.getDigType() == WrappedPacketInBlockDig.PlayerDigType.STOP_DESTROY_BLOCK) {
                if (this.increaseBuffer() > 2.0) {
                    this.fail("Tried to break blocks incorrectly (Nuker)", "D=" + wrapped.getDigType());
                }
            }
            else {
                this.decreaseBuffer();
            }
        }
    }
}
