package xyz.edge.ac.cmd.commands;

import org.bukkit.OfflinePlayer;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "clearlogs", syntax = "<player>", purpose = "Clear logs of player!")
public class ClearLogsCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " clearlogs (player)"));
            return false;
        }
        if (!(sender instanceof Player)) {
            final OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
            final File file = new File("plugins/Edge/logs/", p.getUniqueId() + ".txt");
            if (file.exists()) {
                file.delete();
            }
        }
        else {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "Console only!"));
        }
        return true;
    }
}
