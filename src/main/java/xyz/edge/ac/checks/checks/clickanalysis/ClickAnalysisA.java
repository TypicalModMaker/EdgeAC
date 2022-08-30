package xyz.edge.ac.checks.checks.clickanalysis;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "ClickAnalysis (A)", type = "A")
public final class ClickAnalysisA extends EdgeCheck
{
    private int ticks;
    private int cps;
    private static final ConfigValue maxCps;
    
    public ClickAnalysisA(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_FLYING()) {
            this.debug("Clicking too fast in 20 tick &8[&cTC=" + this.cps + "/(T=1)-" + ClickAnalysisA.maxCps.getDouble() + "&8]");
            if (++this.ticks >= 20) {
                this.debug("cps=" + this.cps);
                if (this.cps > ClickAnalysisA.maxCps.getDouble() && !this.isExempt(Exempts.AUTO_CLICKER)) {
                    this.fail("Clicking too fast in 20 tickS", "CPS=" + this.cps + "/(T=20)-" + ClickAnalysisA.maxCps.getDouble());
                }
                final int n = 0;
                this.cps = n;
                this.ticks = n;
            }
        }
        else if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                ++this.cps;
            }
        }
    }
    
    static {
        maxCps = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.max-cps");
    }
}
