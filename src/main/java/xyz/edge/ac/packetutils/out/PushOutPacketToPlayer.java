package xyz.edge.ac.packetutils.out;

import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.gamestatechange.WrappedPacketOutGameStateChange;
import org.bukkit.entity.Player;

public class PushOutPacketToPlayer
{
    public static void sendOutPositionDemoScreen(final Player player) {
        final WrappedPacketOutGameStateChange wrappedPacketOutGameStateChange = new WrappedPacketOutGameStateChange(5, 0.0f);
        PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutGameStateChange);
    }
    
    public static void sendOutPositionCredits(final Player player) {
        final WrappedPacketOutGameStateChange wrappedPacketOutGameStateChange = new WrappedPacketOutGameStateChange(4, 1.0f);
        PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutGameStateChange);
    }
}
