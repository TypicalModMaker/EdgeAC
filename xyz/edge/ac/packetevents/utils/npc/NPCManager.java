package xyz.edge.ac.packetevents.utils.npc;

import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class NPCManager
{
    private final Map<Integer, NPC> npcMap;
    
    public NPCManager() {
        this.npcMap = new ConcurrentHashMap<Integer, NPC>();
    }
    
    @Nullable
    public NPC getNPCById(final int entityID) {
        return this.npcMap.get(entityID);
    }
    
    public Collection<NPC> getNPCList() {
        return this.npcMap.values();
    }
    
    public void registerNPC(final NPC npc) {
        this.npcMap.put(npc.getEntityId(), npc);
    }
    
    public void unregisterNPC(final NPC npc) {
        this.npcMap.remove(npc.getEntityId());
    }
}
