package xyz.edge.ac.packetevents.packetwrappers.play.out.entityequipment;

import org.jetbrains.annotations.Nullable;

public enum EquipmentSlot
{
    MAINHAND, 
    OFFHAND, 
    BOOTS, 
    LEGGINGS, 
    CHESTPLATE, 
    HELMET;
    
    public byte id;
    
    @Nullable
    public static EquipmentSlot getById(final byte id) {
        for (final EquipmentSlot slot : values()) {
            if (slot.id == id) {
                return slot;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.id;
    }
}
