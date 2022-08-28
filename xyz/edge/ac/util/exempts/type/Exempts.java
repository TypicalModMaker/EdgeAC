package xyz.edge.ac.util.exempts.type;

import org.bukkit.util.NumberConversions;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.util.utils.ServerUtil;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.Edge;
import xyz.edge.ac.packetevents.utils.player.GameMode;
import xyz.edge.ac.util.utils.PlayerUtil;
import xyz.edge.ac.user.User;
import java.util.function.Function;

public enum Exempts
{
    CHUNK(user -> !user.getPlayer().getWorld().isChunkLoaded(NumberConversions.floor(user.getMovementHandler().getX()) >> 4, NumberConversions.floor(user.getMovementHandler().getZ()) >> 4)), 
    TPS(user -> ServerUtil.getTPS() < 19.0), 
    TELEPORT(user -> user.getMovementHandler().isTeleporting() || System.currentTimeMillis() - user.getJoinTime() < 2000L), 
    TELEPORT_TIME(user -> user.getMovementHandler().getTeleportTicks() < 5), 
    GLIDING(data -> data.getTicks() - data.getGliding().getGlidingTicks() <= 40), 
    SLOW_FALL(user -> ServerUtil.getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_13) && user.getPlayer().hasPotionEffect(PotionEffectType.SLOW_FALLING)), 
    VELOCITY(user -> user.getVelocityHandler().isTakingHorizontal()), 
    VELOCITY_TICK(user -> user.getVelocityHandler().getTicksSinceVelocity() == 1), 
    JOINED(user -> System.currentTimeMillis() - user.getJoinTime() < 5000L), 
    CREATIVE(user -> user.getPlayer().getGameMode() == org.bukkit.GameMode.CREATIVE), 
    JUMP(user -> {
        final boolean onGround = user.getMovementHandler().isOnGround();
        final boolean lastOnGround = user.getMovementHandler().isLastOnGround();
        final double deltaY = user.getMovementHandler().getDeltaY();
        final double lastY = user.getMovementHandler().getLastY();
        final boolean deltaModulo = deltaY % 0.015625 == 0.0;
        final boolean lastGround = lastY % 0.015625 == 0.0;
        final boolean step = deltaModulo && lastGround;
        final double modifierJump = PlayerUtil.getPotionLevel(user.getPlayer(), PotionEffectType.JUMP) * 0.1f;
        final double expectedJumpMotion = 0.41999998688697815 + modifierJump;
        return Boolean.valueOf(Math.abs(expectedJumpMotion - deltaY) < 1.0E-5 && !onGround && lastOnGround && !step);
    }), 
    ELYTA_TICK(user -> user.getTicks() - user.getGliding().getGlidingTicks() < 100), 
    NEAR_WALL(user -> user.getMovementHandler().isNearWall()), 
    STEPPED(user -> user.getMovementHandler().isOnGround() && user.getMovementHandler().getDeltaY() > 0.0), 
    CINEMATIC(user -> user.getRotationHandler().isCinematic()), 
    SLIME(user -> user.getMovementHandler().getSinceSlimeTicks() < 30), 
    ICE(user -> user.getMovementHandler().getSinceIceTicks() < 40), 
    SLAB(user -> user.getMovementHandler().isNearSlab()), 
    STAIRS(user -> user.getMovementHandler().isNearStairs()), 
    WEB(user -> user.getMovementHandler().isInWeb()), 
    BUKKIT_PLACING(user -> Edge.getInstance().getTickManager().getTicks() - user.getPacketActionHandler().getLastBukkitPlaceTick() < 10), 
    CLIMBABLE(user -> user.getMovementHandler().isOnClimbable()), 
    DIGGING(user -> Edge.getInstance().getTickManager().getTicks() - user.getPacketActionHandler().getLastDiggingTick() < 10), 
    BLOCK_BREAK(user -> Edge.getInstance().getTickManager().getTicks() - user.getPacketActionHandler().getLastBreakTick() < 10), 
    PLACING(user -> Edge.getInstance().getTickManager().getTicks() - user.getPacketActionHandler().getLastBukkitPlaceTick() < 10), 
    NEAR_VEHICLE(user -> user.getMovementHandler().isNearVehicle()), 
    INSIDE_VEHICLE(user -> user.getMovementHandler().getSinceVehicleTicks() < 20), 
    LIQUID(user -> user.getMovementHandler().isInLiquid()), 
    UNDER_BLOCK(user -> user.getMovementHandler().isBlockNearHead()), 
    PISTON(user -> user.getMovementHandler().getSinceNearPistonTicks() < 50), 
    VOID(user -> user.getPlayer().getLocation().getY() < 4.0), 
    COMBAT(user -> user.getFightHandler().getHitTicks() < 5), 
    FLYING(user -> user.getTicks() - user.getLol().getFlyingTick() < 40 || user.getLol().getGameMode() == GameMode.SPECTATOR), 
    AUTO_CLICKER(user -> user.getExemptionHandler().isExempt(Exempts.PLACING, Exempts.DIGGING, Exempts.BLOCK_BREAK)), 
    DEPTH_STRIDER(user -> PlayerUtil.getDepthStriderLevel(user.getPlayer()) > 0);
    
    private final Function<User, Boolean> exception;
    
    private Exempts(final Function<User, Boolean> exception) {
        this.exception = exception;
    }
    
    public Function<User, Boolean> getException() {
        return this.exception;
    }
}
