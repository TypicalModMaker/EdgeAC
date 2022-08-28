package xyz.edge.ac.packetevents.packetwrappers.play.in.jigsawgenerate;

import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInJigsawGenerate extends WrappedPacket
{
    public WrappedPacketInJigsawGenerate(final NMSPacket packet) {
        super(packet);
    }
    
    public Vector3i getBlockPosition() {
        return this.readBlockPosition(0);
    }
    
    public void setBlockPosition(final Vector3i blockPosition) {
        this.writeBlockPosition(0, blockPosition);
    }
    
    public int getLevels() {
        return this.readInt(0);
    }
    
    public void setLevels(final int levels) {
        this.writeInt(0, levels);
    }
    
    public boolean isKeepingJigsaws() {
        return this.readBoolean(0);
    }
    
    public void setKeepingJigsaws(final boolean keepingJigsaws) {
        this.writeBoolean(0, keepingJigsaws);
    }
}
