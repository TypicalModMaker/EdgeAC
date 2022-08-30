package xyz.edge.ac.checks.checks.invalid;

import org.bukkit.entity.Horse;
import org.bukkit.entity.EntityType;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Invalid (H)", type = "H", experimental = true, exemptBedrock = true)
public class InvalidH extends EdgeCheck
{
    public InvalidH(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_STEER_VEHICLE()) {
            if (this.user.getPlayer().getVehicle() == null) {
                return;
            }
            if (this.user.getPlayer().getVehicle().getType() == EntityType.HORSE) {
                final Horse horse = (Horse)this.user.getPlayer().getVehicle();
                final double deltaY = this.user.getMovementHandler().getDeltaY();
                final double horseY = horse.getJumpStrength();
                this.debug("DY=" + deltaY + " : HY=" + horseY);
            }
        }
    }
}
