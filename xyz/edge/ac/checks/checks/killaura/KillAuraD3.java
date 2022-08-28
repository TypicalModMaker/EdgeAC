package xyz.edge.ac.checks.checks.killaura;

import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "KillAura (D3)", type = "D3")
public class KillAuraD3 extends EdgeCheck
{
    private int legalHits;
    private int illegalHits;
    private int movements;
    private int lastMovements;
    
    public KillAuraD3(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            if (this.movements < 10) {
                if (this.movements == this.lastMovements) {
                    ++this.illegalHits;
                }
                else {
                    ++this.legalHits;
                }
                this.debug("Advanced accuracy check for 80%+ &8[&cIH(r)=" + this.illegalHits / 30.0 + " : PM=" + this.movements + "&8]");
                if (this.legalHits + this.illegalHits == 30) {
                    if (this.illegalHits > 24) {
                        final double buffer = this.buffer + 12.0;
                        this.buffer = buffer;
                        if (buffer > 20.0) {
                            this.fail("Advanced accuracy check for 80%+", "IH(r)=" + this.illegalHits / 30.0 + " : PM=" + this.movements);
                        }
                    }
                    else {
                        this.decreaseBufferBy(4.0);
                    }
                    final int n = 0;
                    this.illegalHits = n;
                    this.legalHits = n;
                }
            }
            this.lastMovements = this.movements;
            this.movements = 0;
        }
        else if (packet.PACKET_FLYING()) {
            ++this.movements;
        }
    }
}
