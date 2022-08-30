package xyz.edge.ac.user.impl;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.entity.Player;

public final class Collision
{
    public static void updateCollision(final Player player) {
        final Scoreboard sb = player.getScoreboard();
        if (sb.getEntryTeam(player.getName()) != null) {
            final Team team = sb.getEntryTeam(player.getName());
            assert team != null;
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        else {
            Team t;
            if (sb.getTeam("acollide") != null) {
                t = sb.getTeam("acollide");
            }
            else {
                t = sb.registerNewTeam("acollide");
            }
            assert t != null;
            t.addEntry(player.getName());
            t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }
    
    private Collision() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
