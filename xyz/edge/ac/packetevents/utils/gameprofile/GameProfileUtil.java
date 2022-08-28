package xyz.edge.ac.packetevents.utils.gameprofile;

import xyz.edge.ac.packetevents.utils.player.Skin;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import java.util.UUID;

public class GameProfileUtil
{
    public static Object getGameProfile(final UUID uuid, final String username) {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getGameProfile(uuid, username);
        }
        return GameProfileUtil_8.getGameProfile(uuid, username);
    }
    
    public static WrappedGameProfile getWrappedGameProfile(final Object gameProfile) {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getWrappedGameProfile(gameProfile);
        }
        return GameProfileUtil_8.getWrappedGameProfile(gameProfile);
    }
    
    public static void setGameProfileSkin(final Object gameProfile, final Skin skin) {
        if (NMSUtils.legacyNettyImportMode) {
            GameProfileUtil_7.setGameProfileSkin(gameProfile, skin);
        }
        else {
            GameProfileUtil_8.setGameProfileSkin(gameProfile, skin);
        }
    }
    
    public static Skin getGameProfileSkin(final Object gameProfile) {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getGameProfileSkin(gameProfile);
        }
        return GameProfileUtil_8.getGameProfileSkin(gameProfile);
    }
}
