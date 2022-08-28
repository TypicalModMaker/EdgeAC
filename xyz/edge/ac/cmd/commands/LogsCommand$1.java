package xyz.edge.ac.cmd.commands;

import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import org.bukkit.OfflinePlayer;
import xyz.edge.ac.util.PaginatedOutput;

class LogsCommand$1 extends PaginatedOutput<String> {
    final /* synthetic */ OfflinePlayer val$p;
    
    @Override
    public String getHeader(final int currentPage, final int maxPages) {
        return ColorUtil.translate(Config.BASICLOGSTITLE.replace("%player%", this.val$p.getName()).replace("%page%", currentPage + "").replace("%max_pages%", maxPages + ""));
    }
    
    @Override
    public String format(final String log, final int p1) {
        return ColorUtil.translate(log);
    }
}