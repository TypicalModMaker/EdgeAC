package xyz.edge.ac.checks.checks.clickanalysis;

import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetutils.PacketUtil;
import java.util.ArrayDeque;
import xyz.edge.ac.user.User;
import xyz.edge.ac.util.type.EvictingList;
import java.util.Deque;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "ClickAnalysis (B)", type = "B")
public class ClickAnalysisB extends EdgeCheck
{
    private final Deque<Integer> samples;
    private int ticks;
    private final EvictingList<Integer> clickerData;
    private int delayTime;
    
    public ClickAnalysisB(final User user) {
        super(user);
        this.samples = new ArrayDeque<Integer>();
        this.clickerData = new EvictingList<Integer>(50);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            if (this.delayTime < 5) {
                if (!this.user.getPacketActionHandler().isDigging()) {
                    this.clickerData.add(this.delayTime);
                }
                if (this.clickerData.isFull()) {
                    final double std = MathUtil.getStandardDeviation(this.clickerData);
                    this.debug("Randomised clicks &8[&cSD=" + MathUtil.preciseRound(std, 2) + " : DT=" + this.delayTime + "&8]");
                    if (std < 0.7) {
                        this.fail("Randomised click", "SD=" + MathUtil.preciseRound(std, 2) + " : DT=" + this.delayTime);
                    }
                }
            }
            this.delayTime = 0;
        }
        else if (packet.PACKET_FLYING()) {
            ++this.delayTime;
        }
    }
}
