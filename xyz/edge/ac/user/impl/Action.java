package xyz.edge.ac.user.impl;

import xyz.edge.ac.packetevents.PacketEvents;
import java.util.Collection;
import xyz.edge.ac.util.utils.MathUtil;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.edge.ac.Edge;
import xyz.edge.ac.util.utils.PlayerUtil;
import xyz.edge.ac.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import xyz.edge.ac.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import xyz.edge.ac.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import xyz.edge.ac.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import xyz.edge.ac.util.type.EvictingList;
import xyz.edge.ac.user.User;

public final class Action
{
    private final User user;
    private int lastBukkitPlaceTick;
    private final EvictingList<Long> flyingSamples;
    private boolean sprinting;
    private boolean sneaking;
    private boolean sendingAction;
    private boolean placing;
    private boolean digging;
    private boolean respawning;
    private boolean sendingDig;
    private boolean lagging;
    private boolean bukkitPlacing;
    private boolean inventory;
    private int lastNoSlowPatch;
    private boolean blocking;
    private int heldItemSlot;
    private int lastHeldItemSlot;
    private int lastDiggingTick;
    private int lastBreakTick;
    private int sprintingTicks;
    private int sneakingTicks;
    private long lastFlyingTime;
    private long ping;
    
    public Action(final User user) {
        this.flyingSamples = new EvictingList<Long>(50);
        this.user = user;
    }
    
    public void handleEntityAction(final WrappedPacketInEntityAction wrapper) {
        this.sendingAction = true;
        switch (wrapper.getAction()) {
            case START_SPRINTING: {
                this.sprinting = true;
                break;
            }
            case STOP_SPRINTING: {
                this.sprinting = false;
                break;
            }
            case START_SNEAKING: {
                this.sneaking = true;
                break;
            }
            case STOP_SNEAKING: {
                this.sneaking = false;
                break;
            }
        }
    }
    
    public void handleBlockDig(final WrappedPacketInBlockDig wrapper) {
        this.sendingDig = true;
        switch (wrapper.getDigType()) {
            case START_DESTROY_BLOCK: {
                this.digging = true;
                break;
            }
            case STOP_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK: {
                this.digging = false;
                break;
            }
            case RELEASE_USE_ITEM: {
                this.blocking = true;
                break;
            }
        }
    }
    
    public void handleClientCommand(final WrappedPacketInClientCommand wrapper) {
        switch (wrapper.getClientCommand()) {
            case OPEN_INVENTORY_ACHIEVEMENT: {
                this.inventory = true;
                break;
            }
            case PERFORM_RESPAWN: {
                this.respawning = true;
                break;
            }
        }
    }
    
    public void handleHeldItemSlot(final WrappedPacketInHeldItemSlot wrapper) {
        this.lastHeldItemSlot = this.heldItemSlot;
        this.heldItemSlot = wrapper.getCurrentSelectedSlot();
    }
    
    public void handleBlockPlace() {
        this.placing = true;
    }
    
    public void handleCloseWindow() {
        this.inventory = false;
    }
    
    public void handleArmAnimation() {
        if (this.digging && PlayerUtil.getLookingBlock(this.user.getPlayer(), 5) != null) {
            this.lastDiggingTick = Edge.getInstance().getTickManager().getTicks();
        }
    }
    
    public void handleBukkitPlace() {
        this.bukkitPlacing = true;
    }
    
    public void handleInteract(final PlayerInteractEvent event) {
        if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
            this.lastDiggingTick = Edge.getInstance().getTickManager().getTicks();
        }
    }
    
    public void handleBukkitBlockBreak() {
        this.lastBreakTick = Edge.getInstance().getTickManager().getTicks();
    }
    
    public void handleFlying() {
        this.blocking = false;
        this.sendingDig = false;
        this.sendingAction = false;
        this.placing = false;
        this.respawning = false;
        this.bukkitPlacing = false;
        this.sprintingTicks = (this.sprinting ? (this.sprintingTicks + 1) : 0);
        this.sneakingTicks = (this.sneaking ? (this.sneakingTicks + 1) : 0);
        final long delay = System.currentTimeMillis() - this.lastFlyingTime;
        if (delay > 0L) {
            this.flyingSamples.add(delay);
        }
        if (this.bukkitPlacing) {
            this.lastBukkitPlaceTick = Edge.getInstance().getTickManager().getTicks();
        }
        if (this.flyingSamples.isFull()) {
            final double deviation = MathUtil.getStandardDeviation(this.flyingSamples);
            this.lagging = (deviation > 120.0);
        }
        this.lastFlyingTime = System.currentTimeMillis();
        this.ping = PacketEvents.get().getPlayerUtils().getPing(this.user.getPlayer());
    }
    
    public User getUser() {
        return this.user;
    }
    
    public int getLastBukkitPlaceTick() {
        return this.lastBukkitPlaceTick;
    }
    
    public EvictingList<Long> getFlyingSamples() {
        return this.flyingSamples;
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public boolean isSneaking() {
        return this.sneaking;
    }
    
    public boolean isSendingAction() {
        return this.sendingAction;
    }
    
    public boolean isPlacing() {
        return this.placing;
    }
    
    public boolean isDigging() {
        return this.digging;
    }
    
    public boolean isRespawning() {
        return this.respawning;
    }
    
    public boolean isSendingDig() {
        return this.sendingDig;
    }
    
    public boolean isLagging() {
        return this.lagging;
    }
    
    public boolean isBukkitPlacing() {
        return this.bukkitPlacing;
    }
    
    public boolean isInventory() {
        return this.inventory;
    }
    
    public int getLastNoSlowPatch() {
        return this.lastNoSlowPatch;
    }
    
    public boolean isBlocking() {
        return this.blocking;
    }
    
    public int getHeldItemSlot() {
        return this.heldItemSlot;
    }
    
    public int getLastHeldItemSlot() {
        return this.lastHeldItemSlot;
    }
    
    public int getLastDiggingTick() {
        return this.lastDiggingTick;
    }
    
    public int getLastBreakTick() {
        return this.lastBreakTick;
    }
    
    public int getSprintingTicks() {
        return this.sprintingTicks;
    }
    
    public int getSneakingTicks() {
        return this.sneakingTicks;
    }
    
    public long getLastFlyingTime() {
        return this.lastFlyingTime;
    }
    
    public long getPing() {
        return this.ping;
    }
    
    public void setInventory(final boolean inventory) {
        this.inventory = inventory;
    }
    
    public void setLastNoSlowPatch(final int lastNoSlowPatch) {
        this.lastNoSlowPatch = lastNoSlowPatch;
    }
    
    public void setBlocking(final boolean blocking) {
        this.blocking = blocking;
    }
}
