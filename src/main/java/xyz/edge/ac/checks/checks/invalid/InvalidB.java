package xyz.edge.ac.checks.checks.invalid;

import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (B)", experimental = true, type = "B")
public final class InvalidB extends EdgeCheck
{
    public InvalidB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_POSITION()) {
            if (this.isExempt(Exempts.FLYING, Exempts.TELEPORT, Exempts.GLIDING, Exempts.FLYING, Exempts.CREATIVE)) {
                return;
            }
            final boolean onGround = this.user.getMovementHandler().isOnSolidGround();
            final boolean onLadder = this.user.getMovementHandler().isOnClimbable();
            final double deltaY = this.user.getMovementHandler().getDeltaY();
            final double deltaDeltaY = Math.abs(this.user.getMovementHandler().getDeltaY() - this.user.getMovementHandler().getLastDeltaY());
            final double maximumNonBufferDeltaY = 0.5199999809265137 + PlayerUtil.getPotionLevel(this.user.getPlayer(), PotionEffectType.JUMP) * 0.1;
            final boolean invalid = !onGround && onLadder && deltaDeltaY == 0.0 && deltaY > 0.1177;
            final boolean fuckOff = deltaY > maximumNonBufferDeltaY && onLadder;
            this.debug("Tried to accede climbable speed &8[&cD=" + deltaY + "&8]");
            if (invalid) {
                final double buffer = this.buffer + 1.0;
                this.buffer = buffer;
                if (buffer > 5.0) {
                    this.fail("Tried to accede climbable speed", "DYS=" + deltaY);
                }
            }
            else {
                this.buffer = Math.max(this.buffer - 0.25, 0.0);
            }
            if (fuckOff) {
                this.fail("[!] Tried to accede climbable speed", "DYS=" + deltaY);
            }
        }
    }
}
