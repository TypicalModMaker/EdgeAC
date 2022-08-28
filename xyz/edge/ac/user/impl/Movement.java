package xyz.edge.ac.user.impl;

import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import org.bukkit.plugin.Plugin;
import xyz.edge.ac.Edge;
import org.bukkit.Bukkit;
import java.util.concurrent.FutureTask;
import java.util.function.Predicate;
import xyz.edge.ac.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import org.bukkit.util.NumberConversions;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import xyz.edge.ac.util.block.BlockUtil;
import xyz.edge.ac.util.utils.PlayerUtil;
import org.bukkit.potion.PotionEffectType;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.util.utils.ServerUtil;
import java.util.Iterator;
import xyz.edge.ac.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import java.util.ArrayList;
import java.util.ArrayDeque;
import org.bukkit.block.Block;
import java.util.List;
import xyz.edge.ac.util.type.BoundingBox;
import java.util.Queue;
import org.bukkit.Location;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import xyz.edge.ac.user.User;

public final class Movement
{
    private final User user;
    private double x;
    private double y;
    private double z;
    private double lastX;
    private double lastY;
    private double lastZ;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private double deltaXZ;
    private double lastDeltaX;
    private double lastDeltaZ;
    private double lastDeltaY;
    private double lastDeltaXZ;
    private double accelX;
    private double accelY;
    private double accelZ;
    private double accelXZ;
    private double lastAccelXZ;
    private double motionY;
    private double tpX;
    private double tpY;
    private double tpZ;
    private float friction;
    private float prevFriction;
    private float prevPrevFriction;
    private Vector3d prevLocation;
    private Vector3d location;
    private boolean flying;
    private boolean inVehicle;
    private boolean inLiquid;
    private boolean inAir;
    private boolean inWeb;
    private boolean isRiptide;
    private boolean waterLogged;
    private boolean blockNearHead;
    private boolean onClimbable;
    private boolean onSolidGround;
    private boolean nearVehicle;
    private boolean onSlime;
    private boolean isLeashed;
    private boolean nearPowderSnow;
    private boolean onIce;
    private boolean nearPiston;
    private boolean nearSlab;
    private boolean nearCarpet;
    private boolean nearStairs;
    private boolean nearScaffolding;
    private boolean nearShulker;
    private boolean nearHoneyBlock;
    private boolean nearLilyPad;
    private boolean teleporting;
    private boolean inWater;
    private boolean inLava;
    private boolean onWallOrFence;
    private boolean onSoulSand;
    private boolean hasJumped;
    private boolean jumped;
    private boolean prevOnGround;
    private boolean nearWall;
    private boolean cobbleWall;
    private boolean onSnow;
    private boolean levitation;
    private boolean nearTrapDoor;
    private boolean onVines;
    private int airTicks;
    private int sinceVehicleTicks;
    private int sinceFlyingTicks;
    private int groundTicks;
    private int sinceSlimeTicks;
    private int solidGroundTicks;
    private int sinceDeadTicks;
    private int iceTicks;
    private int sinceIceTicks;
    private int blockNearHeadTicks;
    private int sinceBlockNearHeadTicks;
    private int teleportTicks;
    private int sinceNearPistonTicks;
    private int sinceLiquidTicks;
    private int sinceClimableTicks;
    private int sinceWebTicks;
    private int clientAirTicks;
    private Location safeLocation;
    private final Queue<Vector3d> teleports;
    private BoundingBox boundingBox;
    private boolean onGround;
    private boolean lastOnGround;
    private boolean mathematicallyOnGround;
    private final List<Block> blocks;
    private double lastMoveAngle;
    
    public Movement(final User user) {
        this.prevLocation = new Vector3d(0.0, 0.0, 0.0);
        this.location = new Vector3d(0.0, 0.0, 0.0);
        this.teleports = new ArrayDeque<Vector3d>();
        this.blocks = new ArrayList<Block>();
        this.user = user;
    }
    
    public void handleMyFlyingForTeleportSoWeCanGoFixThisStuff(final WrappedPacketInFlying wrapper) {
        final boolean position = wrapper.isMoving();
        final boolean look = wrapper.isLook();
        final boolean onGround = wrapper.isOnGround();
        this.teleporting = false;
        this.tpX = (position ? wrapper.getX() : this.tpX);
        this.tpY = (position ? wrapper.getY() : this.tpY);
        this.tpZ = (position ? wrapper.getZ() : this.tpZ);
        if (position && look) {
            final Iterator<Vector3d> iterator = this.teleports.iterator();
            if (iterator.hasNext()) {
                do {
                    final Vector3d wantedLocation = iterator.next();
                    if (wantedLocation.getX() == this.tpX && Math.abs(wantedLocation.getY() - this.tpY) < 1.0E-7 && wantedLocation.getZ() == this.tpZ) {
                        this.teleporting = true;
                        this.teleports.remove(wantedLocation);
                        this.teleportTicks = 0;
                        break;
                    }
                } while (iterator.hasNext());
            }
        }
    }
    
    public void handle(final double x, final double y, final double z, final boolean onGround) {
        this.prevPrevFriction = this.prevFriction;
        this.prevFriction = this.friction;
        this.friction = this.handleFriction();
        this.prevOnGround = this.onGround;
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        this.lastOnGround = this.onGround;
        this.hasJumped = this.jumped;
        this.jumped = this.testJump();
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
        this.handleCollisions();
        this.lastDeltaX = this.deltaX;
        this.lastDeltaY = this.deltaY;
        this.lastDeltaZ = this.deltaZ;
        this.lastDeltaXZ = this.deltaXZ;
        this.lastAccelXZ = this.accelXZ;
        final double n = this.y - this.lastY;
        this.deltaY = n;
        this.motionY = n;
        this.deltaX = this.x - this.lastX;
        this.deltaY = this.y - this.lastY;
        this.deltaZ = this.z - this.lastZ;
        this.deltaXZ = Math.hypot(this.deltaX, this.deltaZ);
        this.accelX = Math.abs(this.lastDeltaX - this.deltaX);
        this.accelY = Math.abs(this.lastDeltaY - this.deltaY);
        this.accelZ = Math.abs(this.lastDeltaZ - this.deltaZ);
        this.accelXZ = Math.abs(this.lastDeltaXZ - this.deltaXZ);
        this.prevLocation = this.location;
        this.location = new Vector3d(x, y, z);
        this.mathematicallyOnGround = (y % 0.015625 == 0.0);
        this.isRiptide = (ServerUtil.getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_14) && this.user.getPlayer().isRiptiding());
        this.isLeashed = (ServerUtil.getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_13) && this.user.getPlayer().isLeashed());
        this.levitation = (ServerUtil.getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_9) && this.user.getPlayer().hasPotionEffect(PotionEffectType.LEVITATION));
    }
    
    public void handleTicks() {
        this.groundTicks = ((this.onGround && this.mathematicallyOnGround) ? (this.groundTicks + 1) : 0);
        this.blockNearHeadTicks = (this.blockNearHead ? (this.blockNearHeadTicks + 1) : 0);
        this.sinceNearPistonTicks = (this.nearPiston ? 0 : (this.sinceNearPistonTicks + 1));
        this.sinceBlockNearHeadTicks = (this.blockNearHead ? 0 : (this.sinceBlockNearHeadTicks + 1));
        this.sinceLiquidTicks = (this.isInLiquid() ? 0 : (this.sinceLiquidTicks + 1));
        this.sinceClimableTicks = (this.isOnClimbable() ? 0 : (this.sinceClimableTicks + 1));
        this.airTicks = (this.inAir ? (this.airTicks + 1) : 0);
        this.clientAirTicks = (this.onGround ? 0 : (++this.clientAirTicks));
        this.inVehicle = this.user.getPlayer().isInsideVehicle();
        this.sinceVehicleTicks = (this.inVehicle ? 0 : (this.sinceVehicleTicks + 1));
        this.iceTicks = (this.onIce ? (this.iceTicks + 1) : 0);
        this.sinceIceTicks = (this.onIce ? 0 : (this.sinceIceTicks + 1));
        this.solidGroundTicks = (this.onSolidGround ? (this.solidGroundTicks + 1) : 0);
        this.flying = this.user.getPlayer().isFlying();
        this.sinceFlyingTicks = (this.flying ? 0 : (this.sinceFlyingTicks + 1));
        this.sinceSlimeTicks = (this.onSlime ? 0 : (this.sinceSlimeTicks + 1));
        this.sinceDeadTicks = (this.user.getPlayer().isDead() ? 0 : (this.sinceDeadTicks + 1));
        ++this.teleportTicks;
        this.sinceWebTicks = (this.isInWeb() ? 0 : (this.sinceWebTicks + 1));
    }
    
    public boolean testJump() {
        final boolean onGround = this.isOnGround();
        final boolean lastOnGround = this.isPrevOnGround();
        final double deltaY = this.user.getMovementHandler().getDeltaY();
        final double lastY = this.getLastY();
        final boolean deltaModulo = deltaY % 0.015625 == 0.0;
        final boolean lastGround = lastY % 0.015625 == 0.0;
        final boolean step = deltaModulo && lastGround;
        final double modifierJump = PlayerUtil.getPotionLevel(this.user.getPlayer(), PotionEffectType.JUMP) * 0.1f;
        final double expectedJumpMotion = 0.41999998688697815 + modifierJump;
        return Math.abs(expectedJumpMotion - deltaY) < 1.0E-5 && !onGround && lastOnGround && !step;
    }
    
    public float handleFriction() {
        final Player player = this.user.getPlayer();
        float friction = 0.91f;
        if (this.onGround) {
            final Location location = new Location(player.getWorld(), this.x, this.y, this.z);
            final Block block = BlockUtil.getBlockAsync(new Location(player.getWorld(), location.getX(), location.getY() - 1.0, location.getZ()));
            float sliperiness = 0.6f;
            if (block != null) {
                if (block.getType().toString().equalsIgnoreCase("SLIME_BLOCK")) {
                    sliperiness = 0.8f;
                }
                if (block.getType() == Material.ICE || block.getType() == Material.PACKED_ICE) {
                    sliperiness = 0.98f;
                }
            }
            friction *= sliperiness;
        }
        return friction;
    }
    
    public void handleCollisions() {
        this.blocks.clear();
        final BoundingBox boundingBox = new BoundingBox(this.user).expandSpecific(0.1, 0.1, 0.7, 0.6, 0.1, 0.1);
        this.boundingBox = boundingBox;
        final double minX = boundingBox.getMinX();
        final double minY = boundingBox.getMinY();
        final double minZ = boundingBox.getMinZ();
        final double maxX = boundingBox.getMaxX();
        final double maxY = boundingBox.getMaxY();
        final double maxZ = boundingBox.getMaxZ();
        Block block = null;
        for (double x = minX; x <= maxX; x += maxX - minX) {
            for (double y = minY; y <= maxY + 0.01; y += (maxY - minY) / 5.0) {
                for (double z = minZ; z <= maxZ; z += maxZ - minZ) {
                    final Location location = new Location(this.user.getPlayer().getWorld(), x, y, z);
                    block = this.getBlock(location);
                    if (block != null) {
                        this.blocks.add(block);
                    }
                }
            }
        }
        this.handleClimbableCollision();
        this.handleVehicle();
        this.inWater = this.blocks.stream().anyMatch(block -> block.getType() == Material.WATER);
        this.cobbleWall = this.blocks.stream().allMatch(block -> block.getType().toString().contains("WALL"));
        this.inLava = this.blocks.stream().anyMatch(block -> block.getType() == Material.LAVA);
        this.inLiquid = this.blocks.stream().anyMatch(Block::isLiquid);
        this.inWeb = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("WEB"));
        this.inAir = this.blocks.stream().allMatch(block -> block.getType() == Material.AIR);
        this.onIce = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("ICE"));
        this.onSnow = this.blocks.stream().allMatch(block -> block.getType().toString().contains("SNOW"));
        this.onSolidGround = this.blocks.stream().anyMatch(block -> block.getType().isSolid());
        this.nearSlab = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("SLAB"));
        this.nearCarpet = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("CARPET"));
        this.nearLilyPad = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("LILY"));
        this.nearStairs = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("STAIRS"));
        this.nearScaffolding = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("SCAFFOLDING"));
        this.nearShulker = this.blocks.stream().allMatch(block -> block.getType().toString().contains("shulker_box"));
        this.nearHoneyBlock = this.blocks.stream().allMatch(block -> block.getType().toString().contains("HONEY_BLOCK"));
        this.nearPowderSnow = this.blocks.stream().allMatch(block -> block.getType().toString().contains("Powder"));
        if (ServerUtil.getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_13)) {
            this.waterLogged = this.blocks.stream().allMatch(block -> block.getBlockData() instanceof Waterlogged);
        }
        this.nearTrapDoor = this.blocks.stream().allMatch(block -> block.getType().toString().contains("TRAPDOOR"));
        this.blockNearHead = (this.blocks.stream().filter(block -> block.getLocation().getY() - this.user.getMovementHandler().getY() > 1.7).anyMatch(block -> block.getType() != Material.AIR) || this.isCollidingAtLocation(1.801, material -> material.toString().contains("TRAP_DOOR") || material.toString().contains("TRAPDOOR"), CollisionType.ANY) || this.isCollidingAtLocation(1.801, material -> material == Material.IRON_TRAPDOOR, CollisionType.ANY) || this.isCollidingAtLocation(2.0, material -> material != Material.AIR, CollisionType.ANY));
        this.onSlime = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("SLIME"));
        this.onWallOrFence = (this.blocks.stream().anyMatch(block -> block.getType().toString().contains("WALL")) || this.blocks.stream().anyMatch(block -> block.getType().toString().contains("FENCE")));
        this.onSoulSand = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("SOUL"));
        this.onVines = this.blocks.stream().allMatch(block -> block.getType().toString().contains("vines"));
        this.nearPiston = this.blocks.stream().anyMatch(block -> block.getType().toString().contains("PISTON"));
        this.nearWall = this.blocks.stream().anyMatch(block -> block.getLocation().getBlockY() - this.y <= this.y && block.getLocation().getY() - this.y > -0.05 && block.getType() != Material.AIR);
        this.handleTicks();
    }
    
    public void handleClimbableCollision() {
        final Location location = this.user.getPlayer().getLocation();
        final int var1 = NumberConversions.floor(location.getX());
        final int var2 = NumberConversions.floor(location.getY());
        final int var3 = NumberConversions.floor(location.getZ());
        final Block var4 = this.getBlock(new Location(location.getWorld(), var1, var2, var3));
        this.onClimbable = (var4.getType() == Material.LADDER || var4.getType() == Material.VINE);
    }
    
    public void handleVehicle() {
        this.nearVehicle = PlayerUtil.isNearVehicle(this.user.getPlayer());
    }
    
    public void handleServerPosition(final WrappedPacketOutPosition teleport) {
        final byte relative = teleport.getRelativeFlagsMask();
        Vector3d pos = teleport.getPosition();
        if ((relative & 0x1) == 0x1) {
            pos = pos.add(new Vector3d(this.getX(), 0.0, 0.0));
        }
        if ((relative >> 1 & 0x1) == 0x1) {
            pos = pos.add(new Vector3d(0.0, this.getY(), 0.0));
        }
        if ((relative >> 2 & 0x1) == 0x1) {
            pos = pos.add(new Vector3d(0.0, 0.0, this.getZ()));
        }
        teleport.setPosition(pos);
        teleport.setRelativeFlagsMask((byte)(relative & 0x18));
        if (ServerVersion.getVersion().isOlderThan(ServerVersion.v_1_8)) {
            pos.setY(pos.getY() - 1.62);
        }
        this.teleports.add(pos);
    }
    
    public boolean isCollidingAtLocation(final double drop, final Predicate<Material> predicate, final CollisionType collisionType) {
        final ArrayList<Material> materials = new ArrayList<Material>();
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double z = -0.3; z <= 0.3; z += 0.3) {
                final Material material = this.getBlock(this.user.getPlayer().getLocation().clone().add(x, drop, z)).getType();
                if (material != null) {
                    materials.add(material);
                }
            }
        }
        return materials.stream().allMatch((Predicate<? super Object>)predicate);
    }
    
    public Block getBlock(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getBlock();
        }
        final FutureTask<Block> futureTask = new FutureTask<Block>(() -> {
            location.getWorld().loadChunk(location.getBlockX() >> 4, location.getBlockZ() >> 4);
            return location.getBlock();
        });
        Bukkit.getScheduler().runTask(Edge.getInstance(), futureTask);
        return null;
    }
    
    public boolean isTeleporting() {
        return this.user.getVersion().isOlderThanOrEquals(ClientVersion.v_1_7_10) ? (this.teleports.size() > 0 || this.teleporting) : this.teleporting;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public double getLastX() {
        return this.lastX;
    }
    
    public double getLastY() {
        return this.lastY;
    }
    
    public double getLastZ() {
        return this.lastZ;
    }
    
    public double getDeltaX() {
        return this.deltaX;
    }
    
    public double getDeltaY() {
        return this.deltaY;
    }
    
    public double getDeltaZ() {
        return this.deltaZ;
    }
    
    public double getDeltaXZ() {
        return this.deltaXZ;
    }
    
    public double getLastDeltaX() {
        return this.lastDeltaX;
    }
    
    public double getLastDeltaZ() {
        return this.lastDeltaZ;
    }
    
    public double getLastDeltaY() {
        return this.lastDeltaY;
    }
    
    public double getLastDeltaXZ() {
        return this.lastDeltaXZ;
    }
    
    public double getAccelX() {
        return this.accelX;
    }
    
    public double getAccelY() {
        return this.accelY;
    }
    
    public double getAccelZ() {
        return this.accelZ;
    }
    
    public double getAccelXZ() {
        return this.accelXZ;
    }
    
    public double getLastAccelXZ() {
        return this.lastAccelXZ;
    }
    
    public double getMotionY() {
        return this.motionY;
    }
    
    public double getTpX() {
        return this.tpX;
    }
    
    public double getTpY() {
        return this.tpY;
    }
    
    public double getTpZ() {
        return this.tpZ;
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public float getPrevFriction() {
        return this.prevFriction;
    }
    
    public float getPrevPrevFriction() {
        return this.prevPrevFriction;
    }
    
    public Vector3d getPrevLocation() {
        return this.prevLocation;
    }
    
    public Vector3d getLocation() {
        return this.location;
    }
    
    public boolean isFlying() {
        return this.flying;
    }
    
    public boolean isInVehicle() {
        return this.inVehicle;
    }
    
    public boolean isInLiquid() {
        return this.inLiquid;
    }
    
    public boolean isInAir() {
        return this.inAir;
    }
    
    public boolean isInWeb() {
        return this.inWeb;
    }
    
    public boolean isRiptide() {
        return this.isRiptide;
    }
    
    public boolean isWaterLogged() {
        return this.waterLogged;
    }
    
    public boolean isBlockNearHead() {
        return this.blockNearHead;
    }
    
    public boolean isOnClimbable() {
        return this.onClimbable;
    }
    
    public boolean isOnSolidGround() {
        return this.onSolidGround;
    }
    
    public boolean isNearVehicle() {
        return this.nearVehicle;
    }
    
    public boolean isOnSlime() {
        return this.onSlime;
    }
    
    public boolean isLeashed() {
        return this.isLeashed;
    }
    
    public boolean isNearPowderSnow() {
        return this.nearPowderSnow;
    }
    
    public boolean isOnIce() {
        return this.onIce;
    }
    
    public boolean isNearPiston() {
        return this.nearPiston;
    }
    
    public boolean isNearSlab() {
        return this.nearSlab;
    }
    
    public boolean isNearCarpet() {
        return this.nearCarpet;
    }
    
    public boolean isNearStairs() {
        return this.nearStairs;
    }
    
    public boolean isNearScaffolding() {
        return this.nearScaffolding;
    }
    
    public boolean isNearShulker() {
        return this.nearShulker;
    }
    
    public boolean isNearHoneyBlock() {
        return this.nearHoneyBlock;
    }
    
    public boolean isNearLilyPad() {
        return this.nearLilyPad;
    }
    
    public boolean isInWater() {
        return this.inWater;
    }
    
    public boolean isInLava() {
        return this.inLava;
    }
    
    public boolean isOnWallOrFence() {
        return this.onWallOrFence;
    }
    
    public boolean isOnSoulSand() {
        return this.onSoulSand;
    }
    
    public boolean isHasJumped() {
        return this.hasJumped;
    }
    
    public boolean isJumped() {
        return this.jumped;
    }
    
    public boolean isPrevOnGround() {
        return this.prevOnGround;
    }
    
    public boolean isNearWall() {
        return this.nearWall;
    }
    
    public boolean isCobbleWall() {
        return this.cobbleWall;
    }
    
    public boolean isOnSnow() {
        return this.onSnow;
    }
    
    public boolean isLevitation() {
        return this.levitation;
    }
    
    public boolean isNearTrapDoor() {
        return this.nearTrapDoor;
    }
    
    public boolean isOnVines() {
        return this.onVines;
    }
    
    public int getAirTicks() {
        return this.airTicks;
    }
    
    public int getSinceVehicleTicks() {
        return this.sinceVehicleTicks;
    }
    
    public int getSinceFlyingTicks() {
        return this.sinceFlyingTicks;
    }
    
    public int getGroundTicks() {
        return this.groundTicks;
    }
    
    public int getSinceSlimeTicks() {
        return this.sinceSlimeTicks;
    }
    
    public int getSolidGroundTicks() {
        return this.solidGroundTicks;
    }
    
    public int getSinceDeadTicks() {
        return this.sinceDeadTicks;
    }
    
    public int getIceTicks() {
        return this.iceTicks;
    }
    
    public int getSinceIceTicks() {
        return this.sinceIceTicks;
    }
    
    public int getBlockNearHeadTicks() {
        return this.blockNearHeadTicks;
    }
    
    public int getSinceBlockNearHeadTicks() {
        return this.sinceBlockNearHeadTicks;
    }
    
    public int getTeleportTicks() {
        return this.teleportTicks;
    }
    
    public int getSinceNearPistonTicks() {
        return this.sinceNearPistonTicks;
    }
    
    public int getSinceLiquidTicks() {
        return this.sinceLiquidTicks;
    }
    
    public int getSinceClimableTicks() {
        return this.sinceClimableTicks;
    }
    
    public int getSinceWebTicks() {
        return this.sinceWebTicks;
    }
    
    public int getClientAirTicks() {
        return this.clientAirTicks;
    }
    
    public Location getSafeLocation() {
        return this.safeLocation;
    }
    
    public Queue<Vector3d> getTeleports() {
        return this.teleports;
    }
    
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public boolean isLastOnGround() {
        return this.lastOnGround;
    }
    
    public boolean isMathematicallyOnGround() {
        return this.mathematicallyOnGround;
    }
    
    public List<Block> getBlocks() {
        return this.blocks;
    }
    
    public double getLastMoveAngle() {
        return this.lastMoveAngle;
    }
    
    public void setSafeLocation(final Location safeLocation) {
        this.safeLocation = safeLocation;
    }
    
    public void setLastMoveAngle(final double lastMoveAngle) {
        this.lastMoveAngle = lastMoveAngle;
    }
    
    public enum CollisionType
    {
        ANY, 
        ALL;
    }
}
