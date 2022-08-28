package xyz.edge.ac.packetevents.injector;

import xyz.edge.ac.packetevents.event.impl.PlayerEjectEvent;
import xyz.edge.ac.packetevents.event.PacketEvent;
import xyz.edge.ac.packetevents.event.impl.PlayerInjectEvent;
import org.bukkit.entity.Player;
import xyz.edge.ac.packetevents.injector.modern.late.LateChannelInjectorModern;
import xyz.edge.ac.packetevents.injector.legacy.late.LateChannelInjectorLegacy;
import xyz.edge.ac.packetevents.injector.modern.early.EarlyChannelInjectorModern;
import xyz.edge.ac.packetevents.injector.legacy.early.EarlyChannelInjectorLegacy;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;

public class GlobalChannelInjector
{
    private ChannelInjector injector;
    
    public void load() {
        final boolean legacy = NMSUtils.legacyNettyImportMode;
        if (!PacketEvents.get().getSettings().shouldUseCompatibilityInjector()) {
            this.injector = (legacy ? new EarlyChannelInjectorLegacy() : new EarlyChannelInjectorModern());
        }
        else {
            this.injector = (legacy ? new LateChannelInjectorLegacy() : new LateChannelInjectorModern());
        }
    }
    
    public boolean isBound() {
        return this.injector.isBound();
    }
    
    public void inject() {
        try {
            this.injector.inject();
        }
        catch (final Exception ex) {
            if (this.injector instanceof EarlyInjector) {
                PacketEvents.get().getSettings().compatInjector(true);
                this.load();
                this.injector.inject();
                PacketEvents.get().getPlugin().getLogger().warning("[packetevents] Failed to inject with the Early Injector. Reverting to the Compatibility/Late Injector... This is just a warning, but please report this!");
                ex.printStackTrace();
            }
        }
    }
    
    public void eject() {
        this.injector.eject();
    }
    
    public void injectPlayer(final Player player) {
        final PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        PacketEvents.get().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            this.injector.injectPlayer(player);
        }
    }
    
    public void ejectPlayer(final Player player) {
        final PlayerEjectEvent ejectEvent = new PlayerEjectEvent(player);
        PacketEvents.get().callEvent(ejectEvent);
        if (!ejectEvent.isCancelled()) {
            this.injector.ejectPlayer(player);
        }
    }
    
    public boolean hasInjected(final Player player) {
        return this.injector.hasInjected(player);
    }
    
    public void writePacket(final Object ch, final Object rawNMSPacket) {
        this.injector.writePacket(ch, rawNMSPacket);
    }
    
    public void flushPackets(final Object ch) {
        this.injector.flushPackets(ch);
    }
    
    public void sendPacket(final Object ch, final Object rawNMSPacket) {
        this.injector.sendPacket(ch, rawNMSPacket);
    }
}
