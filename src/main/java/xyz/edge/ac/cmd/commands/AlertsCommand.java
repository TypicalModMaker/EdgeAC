package xyz.edge.ac.cmd.commands;

import xyz.edge.ac.user.User;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import xyz.edge.ac.Edge;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

public final class AlertsCommand extends Command
{
    public AlertsCommand() {
        super("alerts");
    }
    
    public void handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("edge.alerts")) {
                final Player player = (Player)sender;
                final User user = Edge.getInstance().getUserManager().getPlayeruser(player);
                user.setAlerts(!user.isAlerts());
                user.getPlayer().sendMessage(ColorUtil.translate(user.isAlerts() ? Config.ALERTS_ENABLED : Config.ALERTS_DISABLED));
            }
            else {
                final Player sender2 = (Player)sender;
                sender2.sendMessage(ColorUtil.translate(Config.PERMISSION));
            }
        }
        else {
            sender.sendMessage(ColorUtil.translate("&cOnly players can execute this command!"));
        }
    }
    
    public boolean execute(final CommandSender commandSender, final String s, final String[] strings) {
        this.handle(commandSender, this, s, strings);
        return true;
    }
}
