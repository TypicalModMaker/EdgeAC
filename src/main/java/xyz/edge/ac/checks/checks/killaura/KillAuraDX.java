package xyz.edge.ac.checks.checks.killaura;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "KillAura (DX)", type = "DX", licenseType = LicenseType.ENTERPRISE)
public class KillAuraDX extends EdgeCheck
{
    private int ticks;
    private int invalidTicks;
    private int lastTicks;
    private int totalTicks;
    
    public KillAuraDX(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            ++this.ticks;
        }
        else if (packet.PACKET_USE_ENTITY()) {
            if (this.ticks <= 8) {
                if (this.lastTicks == this.ticks) {
                    ++this.invalidTicks;
                }
                this.debug("Impossible hit advantage ratio [AI]");
                if (++this.totalTicks >= 25) {
                    if (this.invalidTicks > 22) {
                        this.fail("Analysis check on player flights using AI", "AI=true : USE=2 : DATA=true : STATE=2 : V=lw : W=6 : WA-MA+OC=2T2LW - MAX-23.1");
                    }
                    this.totalTicks = 0;
                    this.invalidTicks = 0;
                }
                this.lastTicks = this.ticks;
            }
            this.ticks = 0;
        }
    }
}
