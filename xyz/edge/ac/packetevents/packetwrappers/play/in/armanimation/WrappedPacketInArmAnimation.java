package xyz.edge.ac.packetevents.packetwrappers.play.in.armanimation;

import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.player.Hand;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInArmAnimation extends WrappedPacket
{
    private static boolean v_1_9;
    
    public WrappedPacketInArmAnimation(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInArmAnimation.v_1_9 = WrappedPacketInArmAnimation.version.isNewerThanOrEquals(ServerVersion.v_1_9);
    }
    
    public Hand getHand() {
        if (WrappedPacketInArmAnimation.v_1_9) {
            final Enum<?> enumConst = this.readEnumConstant(0, NMSUtils.enumHandClass);
            return Hand.values()[enumConst.ordinal()];
        }
        return Hand.MAIN_HAND;
    }
    
    public void setHand(final Hand hand) {
        if (WrappedPacketInArmAnimation.v_1_9) {
            final Enum<?> enumConst = EnumUtil.valueByIndex(NMSUtils.enumHandClass, hand.ordinal());
            this.writeEnumConstant(0, enumConst);
        }
    }
}
