package xyz.edge.ac.checks.checks.scaffold;

import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.potion.PotionEffectType;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Scaffold (D)", experimental = true, type = "D")
public final class ScaffoldD extends EdgeCheck
{
    private int movements;
    private int lastX;
    private int lastY;
    private int lastZ;
    
    public ScaffoldD(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_BLOCKPLACE()) {
            final WrappedPacketInBlockPlace wrapper = new WrappedPacketInBlockPlace(packet.getRawPacket());
            if (PlayerUtil.getPotionLevel(this.user.getPlayer(), PotionEffectType.JUMP) > 0) {
                return;
            }
            if (this.user.getMovementHandler().getDeltaY() <= 0.0) {
                return;
            }
            if (this.isExempt(Exempts.VELOCITY, Exempts.TELEPORT, Exempts.FLYING)) {
                return;
            }
            this.debug("Tried to accelerate up fast &8[&cM=" + this.movements + " : LYRD=" + this.lastY + "&8]");
            if ((wrapper.getBlockPosition().getX() != 1 || wrapper.getBlockPosition().getY() != 1 || wrapper.getBlockPosition().getZ() == 1) && this.user.getPlayer().getItemInHand().getType().isBlock()) {
                if (this.lastX == wrapper.getBlockPosition().getX() && wrapper.getBlockPosition().getY() > this.lastY && this.lastZ == wrapper.getBlockPosition().getZ()) {
                    if (this.movements < 7) {
                        final double buffer = this.buffer + 1.0;
                        this.buffer = buffer;
                        if (buffer > 2.0) {
                            this.fail("Tried to accelerate up fast", "M=" + this.movements + " : LY=" + this.lastY);
                        }
                    }
                    else {
                        this.buffer = 0.0;
                    }
                    this.movements = 0;
                }
                this.lastX = wrapper.getBlockPosition().getX();
                this.lastY = wrapper.getBlockPosition().getY();
                this.lastZ = wrapper.getBlockPosition().getZ();
            }
        }
        else if (packet.PACKET_FLYING()) {
            ++this.movements;
        }
    }
}
