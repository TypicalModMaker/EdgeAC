package xyz.edge.ac.checks.checks.clickanalysis;

import xyz.edge.ac.util.type.Pair;
import java.util.List;
import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import com.google.common.collect.Lists;
import xyz.edge.ac.user.User;
import java.util.Deque;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "ClickAnalysis (D)", type = "D")
public class ClickAnalysisD extends EdgeCheck
{
    private final Deque<Integer> samples;
    private int ticks;
    
    public ClickAnalysisD(final User user) {
        super(user);
        this.samples = (Deque<Integer>)Lists.newLinkedList();
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_ARM_ANIMATION()) {
            final boolean valid = this.ticks < 4 && !this.isExempt(Exempts.AUTO_CLICKER) && this.ticks != 0;
            if (valid) {
                this.samples.add(this.ticks);
            }
            if (this.samples.size() == 15) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(this.samples);
                final double deviation = MathUtil.getStandardDeviation(this.samples);
                final double outliers = outlierPair.getX().size() + outlierPair.getY().size();
                final double cps = this.user.getClickHandler().getCps();
                final String debug = String.format("deviation=%.2f, outliers=%.2f, cps%.2f", deviation, outliers, cps);
                this.debug("Checks for click rounding &8[&cD=" + deviation + " : O=" + outliers + "&8]");
                if (deviation < 0.3 && outliers < 2.0 && cps % 1.0 == 0.0) {
                    this.buffer += 0.25;
                    if (this.buffer > 0.75) {
                        this.fail("Checks for click rounding", "D=" + deviation + " : O=" + outliers);
                    }
                }
                else {
                    this.buffer = Math.max(this.buffer - 0.2, 0.0);
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
