package xyz.edge.ac.cmd;

import java.lang.annotation.Annotation;
import xyz.edge.ac.util.color.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.List;

public abstract class EdgeCommand implements Comparable<EdgeCommand>
{
    private List<EdgeCommand> commands;
    
    protected abstract boolean handle(final CommandSender p0, final Command p1, final String p2, final String[] p3);
    
    public void sendRetardedNewLine(final CommandSender sender) {
        sender.sendMessage("");
    }
    
    public void sendMessage(final CommandSender sender, final String message) {
        sender.sendMessage(ColorUtil.translate(message));
    }
    
    public CommandInfo getCommandInfo() {
        if (this.getClass().isAnnotationPresent(CommandInfo.class)) {
            return this.getClass().getAnnotation(CommandInfo.class);
        }
        System.err.println("CommandInfo annotation hasn't been added to the class " + this.getClass().getSimpleName() + ".");
        return null;
    }
    
    @Override
    public int compareTo(final EdgeCommand o) {
        return 0;
    }
}
