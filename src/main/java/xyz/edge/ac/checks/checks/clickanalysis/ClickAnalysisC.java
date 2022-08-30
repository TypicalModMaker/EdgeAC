package xyz.edge.ac.checks.checks.clickanalysis;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import java.util.ArrayDeque;
import xyz.edge.ac.user.User;
import java.util.Deque;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "ClickAnalysis (C)", type = "C")
public class ClickAnalysisC extends EdgeCheck
{
    private final Deque<Integer> samples;
    private int ticks;
    
    public ClickAnalysisC(final User user) {
        super(user);
        this.samples = new ArrayDeque<Integer>();
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ARM_ANIMATION() && !this.isExempt(Exempts.AUTO_CLICKER)) {
            if (this.ticks > 50 || this.ticks == 0) {
                this.samples.clear();
            }
            else {
                this.samples.add(this.ticks * 50);
            }
            if (this.samples.size() == 30) {
                final double kurtosis = MathUtil.getKurtosis(this.samples);
                final Entity target = this.user.getFightHandler().getTarget();
                final boolean instanceOfPlayer = !(target instanceof Player);
                final boolean invalid = Double.isNaN(kurtosis);
                this.debug("Impossible click kurtosis &8[&cK=" + kurtosis + " : S=" + this.samples.size() + "&8]");
                if (invalid && instanceOfPlayer) {
                    if (this.increaseBuffer() > 2.0) {
                        this.fail("Impossible click kurtosis", "K=" + kurtosis + " : S=" + this.samples.size());
                    }
                }
                else {
                    this.resetBuffer();
                }
                this.samples.clear();
            }
            this.ticks = 0;
        }
        else if (packet.PACKET_FLYING()) {
            ++this.ticks;
        }
    }
}
