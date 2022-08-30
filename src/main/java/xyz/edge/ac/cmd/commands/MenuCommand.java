package xyz.edge.ac.cmd.commands;

import org.bukkit.entity.HumanEntity;
import xyz.edge.ac.util.menu.DevMenu;
import org.bukkit.entity.Player;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "menu", syntax = "", purpose = "Open menu")
public class MenuCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " menu"));
            return false;
        }
        if (sender.hasPermission("edge.menu")) {
            final DevMenu edgeMenu = new DevMenu((HumanEntity)sender);
            edgeMenu.openGui();
        }
        else {
            sender.sendMessage(ColorUtil.translate(Config.PERMISSION.replaceAll("%permission%", "edge.menu")));
        }
        return true;
    }
}
