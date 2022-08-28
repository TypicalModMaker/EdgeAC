package xyz.edge.ac.banwave;

import xyz.edge.ac.checks.EdgeCheck;
import java.util.UUID;

public class BanwaveEntry
{
    private final long addedAt;
    private final UUID offender;
    private final EdgeCheck check;
    
    public long getAddedAt() {
        return this.addedAt;
    }
    
    public UUID getOffender() {
        return this.offender;
    }
    
    public EdgeCheck getCheck() {
        return this.check;
    }
    
    public BanwaveEntry(final long addedAt, final UUID offender, final EdgeCheck check) {
        this.addedAt = addedAt;
        this.offender = offender;
        this.check = check;
    }
}
