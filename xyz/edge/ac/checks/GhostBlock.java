package xyz.edge.ac.checks;

import java.util.Iterator;
import xyz.edge.ac.user.User;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.config.Config;
import xyz.edge.ac.Edge;
import xyz.edge.ac.packetevents.packettype.PacketType;
import xyz.edge.ac.packetevents.event.impl.PacketPlayReceiveEvent;
import xyz.edge.ac.packetevents.event.PacketListenerDynamic;

public final class GhostBlock extends PacketListenerDynamic
{
    private double buffer;
    
    private double increaseBuffer() {
        return this.buffer = Math.min(10000.0, this.buffer + 1.0);
    }
    
    public double decreaseBufferBy(final double amount) {
        return this.buffer = Math.max(0.0, this.buffer - amount);
    }
    
    public void resetBuffer() {
        this.buffer = 0.0;
    }
    
    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
        if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
            final User user = Edge.getInstance().getUserManager().getPlayeruser(event.getPlayer());
            if (user == null) {
                return;
            }
            if (user.getPlayer() != null && Config.GHOSTENABLED) {
                final double deltaY = user.getMovementHandler().getDeltaY();
                final int airTicksModifier = PlayerUtil.getPotionLevel(user.getPlayer(), PotionEffectType.JUMP);
                final int airTicksLimit = Config.GHOSTTICKS + airTicksModifier;
                final int clientAirTicks = user.getMovementHandler().getAirTicks();
                if (user.getVelocityHandler().isTakingVelocity()) {
                    return;
                }
                if (user.getMovementHandler().isWaterLogged()) {
                    return;
                }
                if (user.getMovementHandler().isNearScaffolding()) {
                    return;
                }
                if (user.getExemptionHandler().isExempt(Exempts.FLYING, Exempts.CREATIVE, Exempts.CLIMBABLE, Exempts.SLIME, Exempts.SLAB, Exempts.STAIRS, Exempts.GLIDING)) {
                    return;
                }
                if (deltaY > 0.0 && clientAirTicks > airTicksLimit && this.increaseBuffer() > 2.0) {
                    if (Config.GHOSTUPDATE) {
                        Bukkit.getScheduler().runTask(Edge.getInstance(), () -> {
                            synchronized (user.getMovementHandler().getBlocks()) {
                                user.getMovementHandler().getBlocks().iterator();
                                final Iterator iterator;
                                while (iterator.hasNext()) {
                                    final Block block = iterator.next();
                                    user.getPlayer().sendBlockChange(block.getLocation(), block.getType(), block.getData());
                                }
                            }
                            return;
                        });
                        if (Config.GHOSTSETBACK) {
                            user.dragDown();
                        }
                        this.resetBuffer();
                    }
                    else {
                        this.decreaseBufferBy(0.1);
                    }
                }
            }
        }
    }
}
