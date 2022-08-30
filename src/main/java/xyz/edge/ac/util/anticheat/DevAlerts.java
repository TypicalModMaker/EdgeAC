package xyz.edge.ac.util.anticheat;

import java.util.HashSet;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.checks.EdgeCheck;
import xyz.edge.ac.user.User;
import java.util.Set;

public final class DevAlerts
{
    private static final Set<User> alerts;
    
    public static ToggleDevAlerts toggleDevAlert(final User user) {
        if (DevAlerts.alerts.contains(user)) {
            DevAlerts.alerts.remove(user);
            return ToggleDevAlerts.REMOVE;
        }
        DevAlerts.alerts.add(user);
        return ToggleDevAlerts.ADD;
    }
    
    public static void handleDevAlert(final EdgeCheck check, final User user, final String info) {
        DevAlerts.alerts.forEach(player -> player.getPlayer().sendMessage(ColorUtil.translate("&7[Dev] &c" + user.getPlayer().getName() + " &7has failed &c" + check.getCheckInfo().name() + " &7info: " + info)));
    }
    
    private DevAlerts() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        alerts = new HashSet<User>();
    }
    
    public enum ToggleDevAlerts
    {
        ADD, 
        REMOVE;
    }
}
