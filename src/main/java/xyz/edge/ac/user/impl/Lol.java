package xyz.edge.ac.user.impl;

import io.github.retrooper.packetevents.packetwrappers.play.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.play.out.gamestatechange.WrappedPacketOutGameStateChange;
import io.github.retrooper.packetevents.utils.player.GameMode;
import xyz.edge.ac.user.User;

public class Lol
{
    private final User user;
    private GameMode gameMode;
    private boolean canFly;
    private boolean flying;
    private int flyingTick;
    
    public void handleGamemode(final WrappedPacketOutGameStateChange wrappedPacketOutGameStateChange) {
        this.user.getTransactionsHandler().sendTransaction();
        this.user.getTransactionsHandler().addTransactionHandler(this.user.getTransactionsHandler().getLastTransactionSent().get(), () -> this.gameMode = GameMode.valueOf(GameMode.values()[(int)wrappedPacketOutGameStateChange.getValue()].name()));
    }
    
    public void onFlying() {
        if (this.canFly) {
            this.flyingTick = this.user.getTicks();
        }
    }
    
    public void handleAbilities(final WrappedPacketOutAbilities abilities) {
        this.user.getTransactionsHandler().addTransactionHandler(this.user.getTransactionsHandler().getLastTransactionSent().get(), () -> {
            this.canFly = abilities.isFlightAllowed();
            this.flying = abilities.isFlying();
        });
    }
    
    public Lol(final User user) {
        this.gameMode = GameMode.SURVIVAL;
        this.user = user;
    }
    
    public GameMode getGameMode() {
        return this.gameMode;
    }
    
    public boolean isCanFly() {
        return this.canFly;
    }
    
    public boolean isFlying() {
        return this.flying;
    }
    
    public int getFlyingTick() {
        return this.flyingTick;
    }
}
