package xyz.edge.ac.user.impl;

import io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata.WrappedWatchableObject;
import java.lang.reflect.Constructor;
import java.util.List;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import xyz.edge.ac.util.ticking.WatchableIndexUtil;
import java.lang.reflect.InvocationTargetException;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.entity.Entity;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import java.util.Collection;
import java.util.ArrayList;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata.WrappedPacketOutEntityMetadata;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import org.bukkit.inventory.ItemStack;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import xyz.edge.ac.user.User;

public final class Gliding
{
    private final User user;
    private boolean gliding;
    private int glidingTicks;
    
    public Gliding(final User user) {
        this.user = user;
    }
    
    public void flying() {
        if (this.gliding) {
            this.glidingTicks = this.user.getTicks();
        }
    }
    
    public void onAction(final WrappedPacketInEntityAction wrappedPacketInEntityAction) {
        if (wrappedPacketInEntityAction.getAction() == WrappedPacketInEntityAction.PlayerAction.START_FALL_FLYING) {
            final ItemStack chestPlate = this.user.getPlayer().getInventory().getChestplate();
            if (chestPlate != null && chestPlate.getType().toString().equals("ELYTRA")) {
                this.gliding = true;
            }
        }
    }
    
    public void onEntityData(final PacketPlaySendEvent event, final WrappedPacketOutEntityMetadata entityMetadata) {
        if (entityMetadata.getEntityId() == this.user.getPlayer().getEntityId()) {
            if (ServerVersion.getVersion().isNewerThanOrEquals(ServerVersion.v_1_14)) {
                final List<Object> metadataStuff = entityMetadata.readList(0);
                final List<Object> metadata = new ArrayList<Object>(metadataStuff);
                metadata.removeIf(element -> {
                    final WrappedPacket wrappedPacket = new WrappedPacket(new NMSPacket(element));
                    final Object dataWatcherObject = wrappedPacket.readAnyObject(0);
                    final WrappedPacket wrappedPacket2 = new WrappedPacket(new NMSPacket(dataWatcherObject));
                    final WrappedPacket wrappedDataWatcher = wrappedPacket2;
                    return wrappedDataWatcher.readInt(0) == 6;
                });
                if (metadata.size() != metadataStuff.size() && !metadata.isEmpty()) {
                    try {
                        final Constructor<?> constructor = event.getNMSPacket().getRawNMSPacket().getClass().getConstructor(Integer.TYPE, NMSUtils.dataWatcherClass, Boolean.TYPE);
                        final Object nmsEntity = NMSUtils.getNMSEntity(event.getPlayer());
                        final Object dataWatcher = NMSUtils.generateDataWatcher(nmsEntity);
                        final Object watcherPacket = constructor.newInstance(this.user.getPlayer().getEntityId(), dataWatcher, true);
                        new WrappedPacket(new NMSPacket(watcherPacket)).writeList(0, metadata);
                        PacketEvents.get().getPlayerUtils().sendNMSPacket(event.getPlayer(), watcherPacket);
                        event.setCancelled(true);
                        return;
                    }
                    catch (final NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            final WrappedWatchableObject watchable = WatchableIndexUtil.getIndex(entityMetadata.getWatchableObjects(), 0);
            if (watchable != null) {
                final Object zeroBitField = watchable.getRawValue();
                if (zeroBitField instanceof Byte) {
                    final byte field = (byte)zeroBitField;
                    final boolean isGliding = (field & 0x80) == 0x80 && this.user.getVersion().isNewerThanOrEquals(ClientVersion.v_1_9);
                    this.user.getTransactionsHandler().sendTransaction();
                    final int transactionSent = this.user.getTransactionsHandler().getLastTransactionSent().get();
                    this.user.getTransactionsHandler().addTransactionHandler(transactionSent, () -> this.gliding = isGliding);
                }
            }
        }
    }
    
    public User getUser() {
        return this.user;
    }
    
    public boolean isGliding() {
        return this.gliding;
    }
    
    public int getGlidingTicks() {
        return this.glidingTicks;
    }
    
    public void setGliding(final boolean gliding) {
        this.gliding = gliding;
    }
}
