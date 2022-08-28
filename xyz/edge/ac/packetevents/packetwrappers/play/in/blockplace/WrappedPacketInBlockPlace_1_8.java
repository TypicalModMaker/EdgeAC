package xyz.edge.ac.packetevents.packetwrappers.play.in.blockplace;

import xyz.edge.ac.packetevents.utils.vector.Vector3f;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

final class WrappedPacketInBlockPlace_1_8 extends WrappedPacket
{
    WrappedPacketInBlockPlace_1_8(final NMSPacket packet) {
        super(packet);
    }
    
    public Vector3i getBlockPosition() {
        return this.readBlockPosition(1);
    }
    
    public void setBlockPosition(final Vector3i blockPos) {
        this.writeBlockPosition(1, blockPos);
    }
    
    public ItemStack getItemStack() {
        return this.readItemStack(0);
    }
    
    public void setItemStack(final ItemStack stack) {
        this.writeItemStack(0, stack);
    }
    
    public int getFace() {
        return this.readInt(0);
    }
    
    public void setFace(final int face) {
        this.writeInt(0, face);
    }
    
    public Vector3f getCursorPosition() {
        return new Vector3f(this.readFloat(0), this.readFloat(1), this.readFloat(2));
    }
    
    public void setCursorPosition(final Vector3f cursorPos) {
        this.writeFloat(0, cursorPos.x);
        this.writeFloat(1, cursorPos.y);
        this.writeFloat(2, cursorPos.z);
    }
}
