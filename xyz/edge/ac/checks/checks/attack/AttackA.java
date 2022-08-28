package xyz.edge.ac.checks.checks.attack;

import xyz.edge.ac.util.mc.reach.SimpleCollisionBox;
import xyz.edge.ac.util.utils.MathUtil;
import xyz.edge.ac.packetevents.utils.player.ClientVersion;
import java.util.Collection;
import java.util.Arrays;
import xyz.edge.ac.util.mc.reach.ReachUtils;
import org.bukkit.util.Vector;
import java.util.Iterator;
import java.util.List;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import org.bukkit.entity.Entity;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import xyz.edge.ac.packetevents.packetwrappers.play.out.entitydestroy.WrappedPacketOutEntityDestroy;
import org.bukkit.GameMode;
import xyz.edge.ac.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import xyz.edge.ac.packetevents.packetwrappers.play.out.entityteleport.WrappedPacketOutEntityTeleport;
import xyz.edge.ac.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import org.bukkit.entity.EntityType;
import xyz.edge.ac.packetevents.packetwrappers.play.out.namedentityspawn.WrappedPacketOutNamedEntitySpawn;
import xyz.edge.ac.packetutils.PacketUtil;
import xyz.edge.ac.user.User;
import java.util.concurrent.ConcurrentLinkedQueue;
import xyz.edge.ac.util.mc.reach.PlayerReachEntity;
import java.util.concurrent.ConcurrentHashMap;
import xyz.edge.ac.config.ConfigValue;
import xyz.edge.api.check.DetectionData;
import xyz.edge.ac.checks.EdgeCheck;

@DetectionData(name = "Attack (A)", type = "A")
public class AttackA extends EdgeCheck
{
    private static final ConfigValue range;
    private static final ConfigValue sightThreshold;
    private static final ConfigValue sightDecay;
    private static final ConfigValue sightEnabled;
    public final ConcurrentHashMap<Integer, PlayerReachEntity> entityMap;
    private final ConcurrentLinkedQueue<Integer> playerAttackQueue;
    private boolean hasSentPreWavePacket;
    private boolean lastPosition;
    private boolean position;
    
    public AttackA(final User user) {
        super(user);
        this.entityMap = new ConcurrentHashMap<Integer, PlayerReachEntity>();
        this.playerAttackQueue = new ConcurrentLinkedQueue<Integer>();
        this.hasSentPreWavePacket = false;
    }
    
    @Override
    public void handle(final PacketUtil packet) {
        if (packet.PACKET_NAMED_ENTITY_SPAWN()) {
            final WrappedPacketOutNamedEntitySpawn spawn = new WrappedPacketOutNamedEntitySpawn(packet.getRawPacket());
            final Entity entity = spawn.getEntity();
            if (entity != null && entity.getType() == EntityType.PLAYER) {
                this.handleSpawnPlayer(spawn.getEntityId(), spawn.getPosition());
            }
        }
        else if (packet.PACKET_REL_MOVE()) {
            final WrappedPacketOutEntity.WrappedPacketOutRelEntityMove move = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(packet.getRawPacket());
            final PlayerReachEntity reachEntity = this.entityMap.get(move.getEntityId());
            if (reachEntity != null) {
                if (reachEntity.lastTransactionHung == this.user.getTransactionsHandler().getLastTransactionSent().get()) {
                    this.user.getTransactionsHandler().sendTransaction();
                }
                reachEntity.lastTransactionHung = this.user.getTransactionsHandler().getLastTransactionSent().get();
                this.handleMoveEntity(move.getEntityId(), move.getDeltaX(), move.getDeltaY(), move.getDeltaZ(), true);
            }
        }
        else if (packet.PACKET_ENTITY_TELEPORT()) {
            final WrappedPacketOutEntityTeleport teleport = new WrappedPacketOutEntityTeleport(packet.getRawPacket());
            final PlayerReachEntity reachEntity = this.entityMap.get(teleport.getEntityId());
            if (reachEntity != null) {
                if (reachEntity.lastTransactionHung == this.user.getTransactionsHandler().getLastTransactionSent().get()) {
                    this.user.getTransactionsHandler().sendTransaction();
                }
                reachEntity.lastTransactionHung = this.user.getTransactionsHandler().getLastTransactionSent().get();
                final Vector3d pos = teleport.getPosition();
                this.handleMoveEntity(teleport.getEntityId(), pos.getX(), pos.getY(), pos.getZ(), false);
            }
        }
        else if (packet.PACKET_USE_ENTITY()) {
            final WrappedPacketInUseEntity action = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (this.user.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (this.user.getMovementHandler().isInVehicle()) {
                return;
            }
            if (action.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                this.checkReach(action.getEntityId());
            }
        }
        else if (packet.PACKET_FLYING()) {
            if (!this.user.getMovementHandler().isTeleporting()) {
                this.position = (packet.getPacketId() == -96 || packet.getPacketId() == -95);
                this.tickFlying();
            }
            this.lastPosition = this.position;
        }
        else if (packet.PACKET_ENTITY_DESTROY()) {
            final WrappedPacketOutEntityDestroy destroy = new WrappedPacketOutEntityDestroy(packet.getRawPacket());
            final int lastTransactionSent = this.user.getTransactionsHandler().getLastTransactionSent().get();
            final Optional<int[]> destroyEntityIds = Optional.ofNullable(destroy.getEntityIds());
            if (!destroyEntityIds.isPresent()) {
                return;
            }
            for (final int integer : destroyEntityIds.get()) {
                final PlayerReachEntity entity2 = this.entityMap.get(integer);
                if (entity2 != null) {
                    entity2.setDestroyed(lastTransactionSent + 1);
                }
            }
        }
        else if (packet.isTransaction()) {
            synchronized (this.entityMap) {
                List<Integer> entitiesToRemove = null;
                for (final Map.Entry<Integer, PlayerReachEntity> entry : this.entityMap.entrySet()) {
                    final PlayerReachEntity entity3 = entry.getValue();
                    if (entity3 == null) {
                        continue;
                    }
                    if (entity3.removeTrans > this.user.getTransactionsHandler().getLastTransactionSent().get()) {
                        continue;
                    }
                    final int entityID = entry.getKey();
                    if (entitiesToRemove == null) {
                        entitiesToRemove = new ArrayList<Integer>();
                    }
                    entitiesToRemove.add(entityID);
                }
                if (entitiesToRemove != null) {
                    for (final int entityID2 : entitiesToRemove) {
                        this.entityMap.remove(entityID2);
                    }
                }
            }
        }
    }
    
    public void tickFlying() {
        final double maxReach = AttackA.range.getDouble();
        for (Integer attackQueue = this.playerAttackQueue.poll(); attackQueue != null; attackQueue = this.playerAttackQueue.poll()) {
            final PlayerReachEntity reachEntity = this.entityMap.get(attackQueue);
            final SimpleCollisionBox targetBox = reachEntity.getPossibleCollisionBoxes();
            targetBox.expand(0.10249999910593033);
            if (!this.lastPosition) {
                targetBox.expand(0.03);
            }
            final Vector3d from = this.user.getMovementHandler().getPrevLocation();
            double minDistance = Double.MAX_VALUE;
            final List<Vector> possibleLookDirs = new ArrayList<Vector>(Arrays.asList(ReachUtils.getLook(this.user.getRotationHandler().getLastYaw(), this.user.getRotationHandler().getLastPitch()), ReachUtils.getLook(this.user.getRotationHandler().getYaw(), this.user.getRotationHandler().getPitch())));
            if (this.user.getVersion().isNewerThanOrEquals(ClientVersion.v_1_9)) {
                possibleLookDirs.add(ReachUtils.getLook(this.user.getRotationHandler().getLastYaw(), this.user.getRotationHandler().getLastPitch()));
            }
            for (final Vector lookVec : possibleLookDirs) {
                for (final double eye : this.user.getPossibleEyeHeights()) {
                    final Vector eyePos = new Vector(from.getX(), from.getY() + eye, from.getZ());
                    final Vector endReachPos = eyePos.clone().add(new Vector(lookVec.getX() * 6.0, lookVec.getY() * 6.0, lookVec.getZ() * 6.0));
                    final Vector intercept = ReachUtils.calculateIntercept(targetBox, eyePos, endReachPos);
                    if (ReachUtils.isVecInside(targetBox, eyePos)) {
                        minDistance = 0.0;
                        break;
                    }
                    if (intercept == null) {
                        continue;
                    }
                    minDistance = Math.min(eyePos.distance(intercept), minDistance);
                }
            }
            if (this.user.getMovementHandler().isTeleporting()) {
                return;
            }
            final boolean reaching = minDistance > maxReach && minDistance < 7.0;
            this.debug("D=" + MathUtil.preciseRound(minDistance, 2));
            if (reaching) {
                this.fail("Attempted to attack out of range", "D=" + MathUtil.preciseRound(minDistance, 2));
            }
            if (minDistance == Double.MAX_VALUE) {
                if (AttackA.sightEnabled.getBoolean() && this.increaseBuffer() > AttackA.sightThreshold.getInt()) {
                    this.fail("Attempted to attacked out of hitbox!", "MD=M_V OF D");
                }
            }
            else {
                this.decreaseBufferBy(AttackA.sightDecay.getDouble());
            }
        }
        for (final PlayerReachEntity entity : this.entityMap.values()) {
            entity.onMovement();
        }
    }
    
    public void checkReach(final int entityID) {
        if (this.entityMap.containsKey(entityID)) {
            this.playerAttackQueue.add(entityID);
        }
    }
    
    private void handleSpawnPlayer(final int playerID, final Vector3d spawnPosition) {
        this.entityMap.put(playerID, new PlayerReachEntity(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ(), this.user));
    }
    
    private void handleMoveEntity(final int entityId, final double deltaX, final double deltaY, final double deltaZ, final boolean isRelative) {
        final PlayerReachEntity reachEntity = this.entityMap.get(entityId);
        if (reachEntity != null) {
            if (!this.hasSentPreWavePacket) {
                this.user.getTransactionsHandler().sendTransaction();
            }
            this.hasSentPreWavePacket = true;
            if (isRelative) {
                reachEntity.serverPos = reachEntity.serverPos.add(new Vector3d(deltaX, deltaY, deltaZ));
            }
            else {
                reachEntity.serverPos = new Vector3d(deltaX, deltaY, deltaZ);
            }
            final int lastTrans = this.user.getTransactionsHandler().getLastTransactionSent().get();
            final Vector3d newPos = reachEntity.serverPos;
            this.user.getTransactionsHandler().addTransactionHandler(lastTrans, () -> reachEntity.onFirstTransaction(newPos.getX(), newPos.getY(), newPos.getZ(), this.user));
            this.user.getTransactionsHandler().addTransactionHandler(lastTrans + 1, reachEntity::onSecondTransaction);
        }
    }
    
    @Override
    public void tickEndEvent() {
        this.user.getTransactionsHandler().sendTransaction();
        this.hasSentPreWavePacket = false;
        super.tickEndEvent();
    }
    
    static {
        range = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.range");
        sightThreshold = new ConfigValue(ConfigValue.ValueType.INTEGER, "settings.sight-threshold");
        sightDecay = new ConfigValue(ConfigValue.ValueType.DOUBLE, "settings.sight-decay");
        sightEnabled = new ConfigValue(ConfigValue.ValueType.BOOLEAN, "settings.sight-detect");
    }
}
