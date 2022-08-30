package xyz.edge.ac.cmd.commands;

import xyz.edge.ac.user.User;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "routerfry", syntax = "", purpose = "sausage")
public class RouterFry extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " routerfry (player)"));
            return false;
        }
        if (sender.hasPermission("edge.routerfry")) {
            final Player p = Bukkit.getPlayer(args[1]);
            final User user = Edge.getInstance().getUserManager().getPlayeruser(p);
            user.routerFry();
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + p.getName() + " router's was fried!"));
        }
        else {
            sender.sendMessage(ColorUtil.translate(Config.PERMISSION.replaceAll("%permission%", "edge.logs")));
        }
        return true;
    }
}
