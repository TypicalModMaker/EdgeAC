package xyz.edge.ac.cmd.commands;

import java.util.Iterator;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.type.Pair;
import java.util.List;
import xyz.edge.ac.checks.EdgeCheck;
import xyz.edge.ac.user.User;
import org.bukkit.entity.Player;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "reboot", syntax = "", purpose = "Reboot command")
public class RebootCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " reboot"));
            return false;
        }
        if (sender.hasPermission("edge.reboot")) {
            final Player playerSender = (Player)sender;
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&cRebooting Edge..."));
            Edge.getInstance().getUserManager().getPlayeruserMap().clear();
            Config.PUNISH_COMMANDS.clear();
            Config.CUSTOMCHECKTYPE.clear();
            Config.CUSTOMCHECKNAME.clear();
            Config.ENABLEDCHECKS.clear();
            Config.BANNINGCHECKS.clear();
            Config.SETBACKCHECKS.clear();
            Config.updateConfig();
            for (final User userA : Edge.getInstance().getUserManager().getPlayeruserMap().values()) {
                for (final EdgeCheck check : userA.getChecks()) {
                    check.hardSetEnabled(Config.ENABLEDCHECKS.contains(this.getClass().getSimpleName()));
                    check.setCustomCheckType(Config.CUSTOMCHECKTYPE.get(this.getClass().getSimpleName()));
                    check.setCustomCheckName(Config.CUSTOMCHECKNAME.get(this.getClass().getSimpleName()));
                    check.setSetback(Config.SETBACKCHECKS.contains(this.getClass().getSimpleName()));
                    check.setCommandVlMap(Config.PUNISH_COMMANDS.get(this.getClass().getSimpleName()));
                    check.setBan(Config.BANNINGCHECKS.contains(this.getClass().getSimpleName()));
                }
            }
            Bukkit.getOnlinePlayers().forEach(player -> Edge.getInstance().getUserManager().add(player));
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&cRebooted Edge! Please wait up to 5 seconds for all checks to become responsive again!"));
            Bukkit.getScheduler().runTaskLater(Edge.getInstance(), new Runnable() {
                @Override
                public void run() {
                    sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&cEdge is now operational! &7All checks, configs, values were rebooted successfully!"));
                    playerSender.sendTitle(ColorUtil.translate("&c\u2713"), ColorUtil.translate(Config.PREFIX + "Rebooted successfully!"));
                }
            }, 100L);
        }
        else {
            sender.sendMessage(ColorUtil.translate(Config.PERMISSION.replaceAll("%permission%", "edge.reboot")));
        }
        return true;
    }
}
