package xyz.edge.ac.cmd.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "alerts", purpose = "Toggle alerts")
public class EdgeAlertsCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender p0, final Command p1, final String p2, final String[] p3) {
        Bukkit.getServer().dispatchCommand(p0, "alerts");
        return true;
    }
}
