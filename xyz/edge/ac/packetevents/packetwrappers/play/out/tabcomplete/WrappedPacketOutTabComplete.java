package xyz.edge.ac.packetevents.packetwrappers.play.out.tabcomplete;

import java.util.List;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutTabComplete extends WrappedPacket
{
    private static boolean v_1_13;
    private static Class<?> suggestionsClass;
    private int transactionID;
    private String[] matches;
    
    public WrappedPacketOutTabComplete(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutTabComplete(final String[] matches) {
        this.transactionID = -1;
        this.matches = matches;
    }
    
    public WrappedPacketOutTabComplete(final int transactionID, final String[] matches) {
        this.transactionID = transactionID;
        this.matches = matches;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutTabComplete.v_1_13 = WrappedPacketOutTabComplete.version.isNewerThanOrEquals(ServerVersion.v_1_13);
        try {
            WrappedPacketOutTabComplete.suggestionsClass = Class.forName("com.mojang.brigadier.suggestion.Suggestions");
        }
        catch (final ClassNotFoundException ex) {}
    }
    
    public Optional<Integer> getTransactionId() {
        if (!WrappedPacketOutTabComplete.v_1_13) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readInt(0));
        }
        return Optional.of(this.transactionID);
    }
    
    public void setTransactionId(final int transactionID) {
        if (WrappedPacketOutTabComplete.v_1_13) {
            if (this.packet != null) {
                this.writeInt(0, transactionID);
            }
            else {
                this.transactionID = transactionID;
            }
        }
    }
    
    public String[] getMatches() {
        if (this.packet == null) {
            return this.matches;
        }
        if (WrappedPacketOutTabComplete.v_1_13) {
            final Object suggestions = this.readObject(0, WrappedPacketOutTabComplete.suggestionsClass);
            final WrappedPacket suggestionsWrapper = new WrappedPacket(new NMSPacket(suggestions));
            final List<Object> suggestionList = suggestionsWrapper.readList(0);
            final String[] matches = new String[suggestionList.size()];
            for (int i = 0; i < matches.length; ++i) {
                final Object suggestion = suggestionList.get(i);
                final WrappedPacket suggestionWrapper = new WrappedPacket(new NMSPacket(suggestion));
                matches[i] = suggestionWrapper.readString(0);
            }
            return matches;
        }
        return this.readStringArray(0);
    }
    
    public void setMatches(final String[] matches) {
        if (this.packet != null) {
            if (WrappedPacketOutTabComplete.v_1_13) {
                final Object suggestions = this.readObject(0, WrappedPacketOutTabComplete.suggestionsClass);
                final WrappedPacket suggestionsWrapper = new WrappedPacket(new NMSPacket(suggestions));
                final List<Object> suggestionList = suggestionsWrapper.readList(0);
                for (int i = 0; i < matches.length; ++i) {
                    final Object suggestion = suggestionList.get(i);
                    final WrappedPacket suggestionWrapper = new WrappedPacket(new NMSPacket(suggestion));
                    suggestionWrapper.writeString(0, matches[i]);
                }
            }
            else {
                this.writeStringArray(0, matches);
            }
        }
        else {
            this.matches = matches;
        }
    }
}
