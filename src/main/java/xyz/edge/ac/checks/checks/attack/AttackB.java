package xyz.edge.ac.checks.checks.attack;

import java.util.Optional;
import org.bukkit.entity.Entity;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.Player;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import xyz.edge.api.license.LicenseType;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Attack (B)", type = "B", licenseType = LicenseType.ENTERPRISE)
public class AttackB extends EdgeCheck
{
    public AttackB(final User user) {
        super(user);
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity wrappedPacketInUseEntity = new WrappedPacketInUseEntity(packet.getRawPacket());
            final Entity entity = wrappedPacketInUseEntity.getEntity();
            if (entity instanceof Player) {
                final Optional<Vector3d> vec3 = wrappedPacketInUseEntity.getTarget();
                if (vec3.isPresent()) {
                    final Vector3d hitbox = vec3.get();
                    this.debug("Tried to attack out of hitbox &8[&cX=" + Math.abs(hitbox.getX()) + " : Z=" + Math.abs(hitbox.getZ()) + " &7]");
                    if (Math.abs(hitbox.getX()) > 0.4001 || Math.abs(hitbox.getZ()) > 0.4001) { // Apparently he worked "hours" on this, or that's what he said to me :joy:
                        this.fail("Tried to attack out of hitbox", "X=" + Math.abs(hitbox.getX()) + " : Z=" + Math.abs(hitbox.getZ()));
                    }
                }
            }
        }
    }
}
