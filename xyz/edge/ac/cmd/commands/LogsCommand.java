package xyz.edge.ac.cmd.commands;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import org.bukkit.OfflinePlayer;
import xyz.edge.ac.util.PaginatedOutput;
import java.util.List;
import java.util.Collections;
import java.io.IOException;
import xyz.edge.ac.util.SystemLogsUtil;
import java.util.Arrays;
import java.io.File;
import org.bukkit.Bukkit;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.Edge;
import xyz.edge.ac.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.edge.ac.cmd.CommandInfo;
import xyz.edge.ac.cmd.EdgeCommand;

@CommandInfo(name = "logs", syntax = "", purpose = "View logs")
public class LogsCommand extends EdgeCommand
{
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&c/" + Edge.getInstance().getCommand().toLowerCase() + " logs (player) [page]"));
            return false;
        }
        int page = 1;
        if (args.length >= 3) {
            try {
                page = Integer.parseInt(args[2]);
            }
            catch (final IllegalArgumentException e) {
                sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&cInvalid page."));
                return true;
            }
        }
        if (sender.hasPermission("edge.logs")) {
            final OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
            String logs = "";
            final File file = new File("plugins/Edge/logs/", p.getUniqueId() + ".txt");
            try {
                logs = getText(file);
            }
            catch (final IOException e2) {
                SystemLogsUtil.createNewLog(Arrays.toString(e2.getStackTrace()), "LogsCommand (GetLogs)", e2.getMessage());
            }
            if (logs.isEmpty()) {
                sender.sendMessage(ColorUtil.translate(Config.NOLOGSFORPLAYER.replace("%player%", p.getName())));
                return false;
            }
            final String[] split = logs.split("\n");
            final List<String> fixedLogs = Arrays.asList(split);
            Collections.reverse(fixedLogs);
            sender.sendMessage(ColorUtil.translate(Config.BREAKER));
            new PaginatedOutput<String>() {
                @Override
                public String getHeader(final int currentPage, final int maxPages) {
                    return ColorUtil.translate(Config.BASICLOGSTITLE.replace("%player%", p.getName()).replace("%page%", currentPage + "").replace("%max_pages%", maxPages + ""));
                }
                
                @Override
                public String format(final String log, final int p1) {
                    return ColorUtil.translate(log);
                }
            }.display(sender, page, fixedLogs);
            sender.sendMessage(ColorUtil.translate(Config.BREAKER));
        }
        else {
            sender.sendMessage(ColorUtil.translate(Config.PERMISSION.replaceAll("%permission%", "edge.logs")));
        }
        return true;
    }
    
    public static String getText(final File file) throws IOException {
        String response = null;
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        final StringBuilder input = new StringBuilder();
        final char[] buffer = new char[1024];
        for (int c = reader.read(buffer); c > 0; c = reader.read(buffer)) {
            for (int i = 0; i < c; ++i) {
                input.append(buffer[i]);
            }
        }
        response = input.toString().replace("\r", "");
        reader.close();
        return response;
    }
}
