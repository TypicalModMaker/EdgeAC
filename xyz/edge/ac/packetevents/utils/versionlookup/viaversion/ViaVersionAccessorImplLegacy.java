package xyz.edge.ac.packetevents.utils.versionlookup.viaversion;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import java.lang.reflect.Method;

public class ViaVersionAccessorImplLegacy implements ViaVersionAccessor
{
    private static Class<?> viaClass;
    private static Method apiAccessor;
    private static Method getPlayerVersionMethod;
    
    @Override
    public int getProtocolVersion(final Player player) {
        if (ViaVersionAccessorImplLegacy.viaClass == null) {
            try {
                ViaVersionAccessorImplLegacy.viaClass = Class.forName("us.myles.ViaVersion.api.Via");
                final Class<?> viaAPIClass = Class.forName("us.myles.ViaVersion.api.ViaAPI");
                ViaVersionAccessorImplLegacy.apiAccessor = ViaVersionAccessorImplLegacy.viaClass.getMethod("getAPI", (Class<?>[])new Class[0]);
                ViaVersionAccessorImplLegacy.getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
            }
            catch (final ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            final Object viaAPI = ViaVersionAccessorImplLegacy.apiAccessor.invoke(null, new Object[0]);
            return (int)ViaVersionAccessorImplLegacy.getPlayerVersionMethod.invoke(viaAPI, player);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
