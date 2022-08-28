package xyz.edge.ac.cmd.commands;

import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

class RebootCommand$1 implements Runnable {
    final /* synthetic */ CommandSender val$sender;
    final /* synthetic */ Player val$playerSender;
    
    @Override
    public void run() {
        this.val$sender.sendMessage(ColorUtil.translate(Config.PREFIX + "&cEdge is now operational! &7All checks, configs, values were rebooted successfully!"));
        this.val$playerSender.sendTitle(ColorUtil.translate("&c\u2713"), ColorUtil.translate(Config.PREFIX + "Rebooted successfully!"));
    }
}