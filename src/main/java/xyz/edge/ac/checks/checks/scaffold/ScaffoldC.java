package xyz.edge.ac.checks.checks.scaffold;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (C)", type = "C")
public final class ScaffoldC extends EdgeCheck
{
    private int looks;
    private int stage;
    
    public ScaffoldC(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_LOOK()) {
            if (this.stage == 0) {
                ++this.stage;
            }
            else if (this.stage == 4) {
                this.debug("Impossible placement order &8[&cS=" + this.stage + " : L=" + this.looks + "&8]");
                final double buffer = this.buffer + 1.75;
                this.buffer = buffer;
                if (buffer > 3.5) {
                    this.fail("Impossible placement order", "S=" + this.stage + " : L=" + this.looks);
                }
                this.stage = 0;
            }
            else {
                this.looks = 0;
                this.stage = 0;
                this.buffer -= 0.2;
            }
        }
        else if (packet.PACKET_BLOCKPLACE()) {
            if (this.stage == 1) {
                ++this.stage;
            }
            else {
                this.looks = 0;
                this.stage = 0;
            }
        }
        else if (packet.PACKET_ARM_ANIMATION()) {
            if (this.stage == 2) {
                ++this.stage;
            }
            else {
                this.looks = 0;
                this.stage = 0;
                this.buffer -= 0.2;
            }
        }
        else if (packet.PACKET_POSITION_LOOK() || packet.PACKET_POSITION()) {
            if (this.stage == 3) {
                if (++this.looks == 3) {
                    this.stage = 4;
                    this.looks = 0;
                }
            }
            else {
                this.looks = 0;
                this.stage = 0;
            }
        }
    }
}
