package xyz.edge.ac.checks.checks.clickanalysis;

import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import java.util.ArrayDeque;
import xyz.edge.ac.user.User;
import java.util.Deque;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "ClickAnalysis (E)", type = "E")
public class ClickAnalysisE extends EdgeCheck
{
    private final Deque<Long> samples;
    private double lastKurtosis;
    private double lastSkewness;
    private double lastDeviation;
    private int ticks;
    
    public ClickAnalysisE(final User user) {
        super(user);
        this.samples = new ArrayDeque<Long>();
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ARM_ANIMATION() && !this.isExempt(Exempts.AUTO_CLICKER) && this.ticks != 0) {
            if (this.ticks > 50) {
                this.samples.clear();
            }
            else {
                this.samples.add(this.ticks * 50L);
            }
            if (this.samples.size() == 30) {
                final double deviation = MathUtil.getStandardDeviation(this.samples);
                final double skewness = MathUtil.getSkewness(this.samples);
                final double kurtosis = MathUtil.getKurtosis(this.samples);
                final boolean invalid = deviation == this.lastDeviation && skewness == this.lastSkewness && kurtosis == this.lastKurtosis;
                this.debug("Weird click statistics &8[&cS=" + skewness + " : K=" + kurtosis + " : S=" + this.samples.size() + "&8]");
                if (invalid) {
                    if (this.increaseBuffer() > 2.0) {
                        this.fail("Weird click statistics", "S=" + skewness + " : K=" + kurtosis + " : S=" + this.samples.size());
                    }
                }
                else {
                    this.resetBuffer();
                }
                this.lastDeviation = deviation;
                this.lastSkewness = skewness;
                this.lastKurtosis = kurtosis;
                this.samples.clear();
            }
            this.ticks = 0;
        }
        else if (packet.PACKET_FLYING()) {
            ++this.ticks;
        }
    }
}
