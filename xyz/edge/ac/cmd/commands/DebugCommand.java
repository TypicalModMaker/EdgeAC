package xyz.edge.ac.cmd.commands;

import java.util.UUID;
import xyz.edge.ac.user.User;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.checks.EdgeCheck;
import xyz.edge.ac.Edge;
import java.util.Arrays;
import xyz.edge.ac.util.injector.Injector;
import xyz.edge.ac.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "debug", purpose = "")
public class DebugCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            this.sendMessage(sender, Config.PLAYER_ONLY);
            return true;
        }
        if (Arrays.stream(Injector.EDGE_DEVELOPERS).noneMatch(uuid -> uuid.toString().equalsIgnoreCase(((Player)sender).getUniqueId().toString()))) {
            this.sendMessage(sender, Config.PREFIX + "&cYou must be an Edge developer to use this command.");
            return false;
        }
        if (args.length < 2) {
            this.sendMessage(sender, Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " debug (check)");
            return false;
        }
        final Player player = (Player)sender;
        final User user = Edge.getInstance().getUserManager().getPlayeruser(player);
        final EdgeCheck edgeCheck = user.getChecks().stream().filter(check -> args[1].equalsIgnoreCase(check.getJustTheName() + "_" + check.getType())).findFirst().orElse(null);
        if (edgeCheck == null) {
            this.sendMessage(sender, Config.PREFIX + "&cCheck '" + args[1] + "' not found.");
            return false;
        }
        user.getDebugging().put(edgeCheck, !user.isDebugging(edgeCheck));
        user.getPlayer().sendMessage(ColorUtil.translate(Config.PREFIX + (user.isDebugging(edgeCheck) ? ("&aYou are now debugging " + edgeCheck.getJustTheName() + " " + edgeCheck.getType() + ".") : ("&cYou are no longer debugging " + edgeCheck.getJustTheName() + " " + edgeCheck.getType() + "."))));
        return true;
    }
}
