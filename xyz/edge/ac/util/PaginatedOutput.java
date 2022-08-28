package xyz.edge.ac.util;

import xyz.edge.ac.util.color.ColorUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.command.CommandSender;
import com.google.common.base.Preconditions;
import xyz.edge.ac.config.Config;

public abstract class PaginatedOutput<T>
{
    private final int resultsPerPage;
    
    public PaginatedOutput() {
        this(Config.LOGSPERPAGE);
    }
    
    public PaginatedOutput(final int resultsPerPage) {
        Preconditions.checkArgument(resultsPerPage > 0);
        this.resultsPerPage = resultsPerPage;
    }
    
    public abstract String getHeader(final int p0, final int p1);
    
    public abstract String format(final T p0, final int p1);
    
    public final void display(final CommandSender sender, final int page, final Collection<? extends T> results) {
        this.display(sender, page, (List<? extends T>)new ArrayList<T>(results));
    }
    
    public final void display(final CommandSender sender, final int page, final List<? extends T> results) {
        if (results.size() == 0) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + Config.LOGNOENTRIES));
            return;
        }
        final int maxPages = results.size() / this.resultsPerPage + 1;
        if (page <= 0 || page > maxPages) {
            sender.sendMessage(ColorUtil.translate(Config.PREFIX + Config.LOGNORESULT));
            return;
        }
        sender.sendMessage(this.getHeader(page, maxPages));
        sender.sendMessage("");
        for (int i = this.resultsPerPage * (page - 1); i < this.resultsPerPage * page && i < results.size(); ++i) {
            sender.sendMessage(this.format(results.get(i), i));
        }
    }
}
