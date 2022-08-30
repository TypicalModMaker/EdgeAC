package xyz.edge.ac.checks.checks.timer;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Timer (B)", type = "B")
public class TimerB extends EdgeCheck
{
    private int trans;
    private int flying;
    private int lastTrans;
    
    public TimerB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING() && !this.isExempt(Exempts.TPS, Exempts.TELEPORT)) {
            ++this.flying;
            this.debug("Impossible Fly-Transaction &7[&cT=" + this.trans + " : F=" + this.flying + "&7]");
            if (this.trans >= 5) {
                final boolean invalid = this.flying > 6 && this.lastTrans == 6 && this.trans == 6;
                if (invalid) {
                    final double buffer = this.buffer;
                    this.buffer = buffer + 1.0;
                    if (buffer > 3.0) {
                        this.fail("Impossible Fly-Transaction", "T=" + this.trans + " : F=" + this.flying);
                    }
                }
                else {
                    this.decreaseBufferBy(1.0);
                }
                this.lastTrans = this.trans;
                this.trans = 0;
                this.flying = 0;
            }
        }
        if (packet.PACKET_OUT_POSITION()) {
            --this.flying;
        }
        if (packet.isTick()) {
            ++this.trans;
        }
    }
}
