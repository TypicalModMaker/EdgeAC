package xyz.edge.ac.packetevents.utils.gameprofile;

import net.minecraft.util.com.mojang.authlib.properties.Property;
import xyz.edge.ac.packetevents.utils.player.Skin;
import org.bukkit.entity.Player;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import org.bukkit.Bukkit;
import java.util.UUID;

class GameProfileUtil_7
{
    public static Object getGameProfile(final UUID uuid, final String username) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            final Object entityHuman = NMSUtils.entityHumanClass.cast(NMSUtils.getEntityPlayer(player));
            final WrappedPacket wrappedEntityPlayer = new WrappedPacket(new NMSPacket(entityHuman), NMSUtils.entityHumanClass);
            return wrappedEntityPlayer.readObject(0, (Class<?>)GameProfile.class);
        }
        return new GameProfile(uuid, username);
    }
    
    public static WrappedGameProfile getWrappedGameProfile(final Object gameProfile) {
        final GameProfile gp = (GameProfile)gameProfile;
        return new WrappedGameProfile(gp.getId(), gp.getName());
    }
    
    public static void setGameProfileSkin(final Object gameProfile, final Skin skin) {
        final GameProfile gp = (GameProfile)gameProfile;
        gp.getProperties().put("textures", new Property(skin.getValue(), skin.getSignature()));
    }
    
    public static Skin getGameProfileSkin(final Object gameProfile) {
        final Property property = ((GameProfile)gameProfile).getProperties().get("textures").iterator().next();
        final String value = property.getValue();
        final String signature = property.getSignature();
        return new Skin(value, signature);
    }
}
