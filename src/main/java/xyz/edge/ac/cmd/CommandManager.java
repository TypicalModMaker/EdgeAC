package xyz.edge.ac.cmd;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import java.lang.reflect.Field;
import org.bukkit.command.CommandMap;
import java.util.Collections;
import xyz.edge.ac.cmd.commands.EdgeAlertsCommand;
import xyz.edge.ac.cmd.commands.DebugCommand;
import xyz.edge.ac.cmd.commands.RouterFry;
import xyz.edge.ac.cmd.commands.RebootCommand;
import xyz.edge.ac.cmd.commands.PlayerCommand;
import xyz.edge.ac.cmd.commands.ClearLogsCommand;
import xyz.edge.ac.cmd.commands.LogsCommand;
import xyz.edge.ac.cmd.commands.MenuCommand;
import java.util.ArrayList;
import xyz.edge.ac.Edge;
import java.util.List;
import org.bukkit.command.Command;

public final class CommandManager extends Command
{
    private final List<EdgeCommand> commands;
    
    public CommandManager(final Edge plugin) {
        super(plugin.getCommand());
        this.setLabel(plugin.getCommand());
        this.setAliases(plugin.getCommandAliases());
        this.setUsage("/" + plugin.getCommand() + " (sub command) [args]");
        (this.commands = new ArrayList<EdgeCommand>()).add(new MenuCommand());
        this.commands.add(new LogsCommand());
        this.commands.add(new ClearLogsCommand());
        this.commands.add(new PlayerCommand());
        this.commands.add(new RebootCommand());
        this.commands.add(new RouterFry());
        this.commands.add(new DebugCommand());
        this.commands.add(new EdgeAlertsCommand());
        Collections.sort(this.commands);
        try {
            final Field field = plugin.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            final CommandMap commandMap = (CommandMap)field.get(plugin.getServer());
            commandMap.register(plugin.getDescription().getName(), this);
            field.setAccessible(false);
        }
        catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        final List<String> tabCompletions = new ArrayList<String>();
        if (args.length <= 1) {
            for (final EdgeCommand edgeCommand : this.commands) {
                if (args.length > 0 && edgeCommand.getCommandInfo().name().startsWith(args[0])) {
                    tabCompletions.add(edgeCommand.getCommandInfo().name());
                }
                else {
                    if (args.length != 0) {
                        continue;
                    }
                    tabCompletions.add(edgeCommand.getCommandInfo().name());
                }
            }
            return tabCompletions;
        }
        return super.tabComplete(sender, alias, args);
    }
    
    public boolean execute(final CommandSender commandSender, final String label, final String[] args) {
        if (commandSender.hasPermission("edge.command") || commandSender.isOp()) {
            if (args.length <= 0) {
                final List<String> msg = Config.PAGE1;
                for (final String message : msg) {
                    commandSender.sendMessage(ColorUtil.translate(message.replaceAll("%version%", Edge.getInstance().getDescription().getVersion())));
                }
                return true;
            }
            boolean anyMatch = false;
            for (final EdgeCommand nullCommand : this.commands) {
                final String commandName = nullCommand.getCommandInfo().name();
                if (commandName.equals(args[0])) {
                    anyMatch = true;
                    if (!nullCommand.handle(commandSender, this, label, args)) {
                        return true;
                    }
                    continue;
                }
            }
            if (!anyMatch) {
                commandSender.sendMessage(ChatColor.RED + "Unknown sub-command.");
                this.execute(commandSender, label, new String[0]);
                return true;
            }
        }
        else {
            final Player player = (Player)commandSender;
            player.sendMessage(ColorUtil.translate(Config.PERMISSION.replaceAll("%permission%", "edge.command")));
        }
        return true;
    }
}
