package xyz.edge.ac.checks.checks.timer;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Timer (A)", type = "A")
public class TimerA extends EdgeCheck
{
    private int sentFlying;
    private long currentFlying;
    private long balance;
    private double buffer;
    
    public TimerA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            if (this.user.getMovementHandler().isTeleporting()) {
                return;
            }
            if (this.isExempt(Exempts.JOINED)) {
                return;
            }
            ++this.sentFlying;
            long lastFlying = 0L;
            if (this.currentFlying == 0L) {
                this.currentFlying = System.currentTimeMillis();
                return;
            }
            lastFlying = this.currentFlying;
            this.currentFlying = System.currentTimeMillis();
            this.balance += 50L - (this.currentFlying - lastFlying);
            this.debug("Impossible balance ratio &8[&cBR=" + this.balance + "&8]");
            if (this.balance > 8L) {
                final double buffer = this.buffer;
                this.buffer = buffer + 1.0;
                if (buffer > 3.0 && this.sentFlying > 100) {
                    this.fail("Impossible balance ratio", "B=" + this.balance);
                    this.buffer = 0.0;
                }
                this.balance = 0L;
            }
            else {
                this.buffer = Math.max(0.0, this.buffer - 0.01);
            }
        }
        else if (packet.PACKET_ENTITY_TELEPORT()) {
            this.balance -= 50L;
        }
    }
}
