package xyz.edge.ac.packetevents.packetwrappers.play.out.blockbreakanimation;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutBlockBreakAnimation extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private static boolean v_1_7_10;
    private Vector3i blockPosition;
    private int destroyStage;
    
    public WrappedPacketOutBlockBreakAnimation(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutBlockBreakAnimation(final int entityID, final Vector3i blockPosition, final int destroyStage) {
        this.entityID = entityID;
        this.blockPosition = blockPosition;
        this.destroyStage = destroyStage;
    }
    
    public WrappedPacketOutBlockBreakAnimation(final Entity entity, final Vector3i blockPosition, final int destroyStage) {
        this.entityID = entity.getEntityId();
        this.entity = entity;
        this.blockPosition = blockPosition;
        this.destroyStage = destroyStage;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutBlockBreakAnimation.v_1_7_10 = WrappedPacketOutBlockBreakAnimation.version.isOlderThan(ServerVersion.v_1_8);
        try {
            WrappedPacketOutBlockBreakAnimation.packetConstructor = PacketTypeClasses.Play.Server.BLOCK_BREAK_ANIMATION.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            try {
                WrappedPacketOutBlockBreakAnimation.packetConstructor = PacketTypeClasses.Play.Server.BLOCK_BREAK_ANIMATION.getConstructor(Integer.TYPE, NMSUtils.blockPosClass, Integer.TYPE);
            }
            catch (final NoSuchMethodException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public Vector3i getBlockPosition() {
        if (this.packet == null) {
            return this.blockPosition;
        }
        if (WrappedPacketOutBlockBreakAnimation.v_1_7_10) {
            final int x = this.readInt(1);
            final int y = this.readInt(2);
            final int z = this.readInt(3);
            return new Vector3i(x, y, z);
        }
        return this.readBlockPosition(0);
    }
    
    public void setBlockPosition(final Vector3i blockPosition) {
        if (this.packet != null) {
            if (WrappedPacketOutBlockBreakAnimation.v_1_7_10) {
                this.writeInt(1, blockPosition.x);
                this.writeInt(2, blockPosition.y);
                this.writeInt(3, blockPosition.z);
            }
            else {
                this.writeBlockPosition(0, blockPosition);
            }
        }
        else {
            this.blockPosition = blockPosition;
        }
    }
    
    public int getDestroyStage() {
        if (this.packet != null) {
            final int index = WrappedPacketOutBlockBreakAnimation.v_1_7_10 ? 4 : 1;
            return this.readInt(index);
        }
        return this.destroyStage;
    }
    
    public void setDestroyStage(final int destroyStage) {
        if (this.packet != null) {
            final int index = WrappedPacketOutBlockBreakAnimation.v_1_7_10 ? 4 : 1;
            this.writeInt(index, destroyStage);
        }
        else {
            this.destroyStage = destroyStage;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final Vector3i blockPosition = this.getBlockPosition();
        if (WrappedPacketOutBlockBreakAnimation.v_1_7_10) {
            return WrappedPacketOutBlockBreakAnimation.packetConstructor.newInstance(this.getEntityId(), blockPosition.x, blockPosition.y, blockPosition.z, this.getDestroyStage());
        }
        final Object nmsBlockPos = NMSUtils.generateNMSBlockPos(blockPosition);
        return WrappedPacketOutBlockBreakAnimation.packetConstructor.newInstance(this.getEntityId(), nmsBlockPos, this.getDestroyStage());
    }
}
