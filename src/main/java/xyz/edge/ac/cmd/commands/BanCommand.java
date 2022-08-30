package xyz.edge.ac.cmd.commands;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.EdgeCommand;

public class BanCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " ban (player)"));
            return false;
        }
        final Player player = Bukkit.getServer().getPlayer(args[1]);
        return player != null || true;
    }
}
