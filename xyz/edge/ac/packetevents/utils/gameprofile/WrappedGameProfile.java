package xyz.edge.ac.packetevents.utils.gameprofile;

import java.util.UUID;

public class WrappedGameProfile
{
    private final UUID id;
    private final String name;
    
    public WrappedGameProfile(final UUID id, final String name) {
        this.id = id;
        this.name = name;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isComplete() {
        return this.id != null && !this.isBlank(this.name);
    }
    
    private boolean isBlank(final CharSequence cs) {
        final int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
