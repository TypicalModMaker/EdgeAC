package xyz.edge.ac.util.ticking;

import java.lang.reflect.Field;
import org.bukkit.plugin.Plugin;
import xyz.edge.ac.Error;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.SystemLogsUtil;
import java.util.Arrays;
import sun.misc.Unsafe;
import java.util.Collections;
import xyz.edge.ac.wrappers.HookedListWrapper;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import java.util.List;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import xyz.edge.ac.checks.EdgeCheck;
import xyz.edge.ac.user.User;
import xyz.edge.ac.Edge;

public class TickEnd
{
    boolean hasTicked;
    
    public TickEnd() {
        this.hasTicked = true;
    }
    
    private static void tickRelMove() {
        CompletableFuture.runAsync(() -> {
            for(User user : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                for(EdgeCheck check : user.getChecks()) {
                    check.tickEndEvent();
                }
            }
        }, Edge.getInstance().getTickEndEvent());
    }
    
    public void start() {
        try {
            final Object connection = NMSUtils.getMinecraftServerConnection();
            final Field connectionsList = Reflection.getField(connection.getClass(), List.class, 1);
            final List<Object> endOfTickObject = (List<Object>)connectionsList.get(connection);
            final List<?> wrapper = Collections.synchronizedList((List<?>)new HookedListWrapper<Object>(endOfTickObject) {
                @Override
                public void onIterator() {
                    TickEnd.this.hasTicked = true;
                    tickRelMove();
                }
            });
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            final Unsafe unsafe = (Unsafe)unsafeField.get(null);
            unsafe.putObject(connection, unsafe.objectFieldOffset(connectionsList), wrapper);
        }
        catch (final NoSuchFieldException | IllegalAccessException e) {
            SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "TickEnd (Start)", e.getMessage());
        }
        Bukkit.getScheduler().runTaskTimer(Edge.getInstance(), () -> {
            if (!this.hasTicked) {
                Error.failedHooKTick();
                tickRelMove();
            }
            this.hasTicked = false;
        }, 2L, 1L);
    }
}
