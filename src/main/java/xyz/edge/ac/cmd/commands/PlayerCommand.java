package xyz.edge.ac.cmd.commands;

import org.bukkit.entity.HumanEntity;
import xyz.edge.ac.util.menu.PlayerMenu;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "player", syntax = "<player>", purpose = "View player shit")
public class PlayerCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " player (player)"));
            return false;
        }
        if (sender.hasPermission("edge.player")) {
            if (args.length == 2) {
                final Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    return false;
                }
                final PlayerMenu playerMenu = new PlayerMenu((HumanEntity)sender, p);
                playerMenu.openGui();
                return true;
            }
        }
        else {
            sender.sendMessage(ColorUtil.translate(Config.PERMISSION.replaceAll("%permission%", "edge.player")));
        }
        return true;
    }
}
