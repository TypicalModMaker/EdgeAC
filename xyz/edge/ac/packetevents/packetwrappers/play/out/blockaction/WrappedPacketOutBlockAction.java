package xyz.edge.ac.packetevents.packetwrappers.play.out.blockaction;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.Material;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutBlockAction extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private Vector3i blockPos;
    private int actionID;
    private int actionData;
    private Material blockType;
    
    public WrappedPacketOutBlockAction(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutBlockAction(final Vector3i blockPos, final int actionID, final int actionData, final Material blockType) {
        this.blockPos = blockPos;
        this.actionID = actionID;
        this.actionData = actionData;
        this.blockType = blockType;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutBlockAction.packetConstructor = PacketTypeClasses.Play.Server.BLOCK_ACTION.getConstructor(NMSUtils.blockPosClass, NMSUtils.blockClass, Integer.TYPE, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public Vector3i getBlockPosition() {
        if (this.packet != null) {
            return this.readBlockPosition(0);
        }
        return this.blockPos;
    }
    
    public void setBlockPosition(final Vector3i blockPos) {
        if (this.packet != null) {
            this.writeBlockPosition(0, blockPos);
        }
        else {
            this.blockPos = blockPos;
        }
    }
    
    public int getActionId() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.actionID;
    }
    
    public void setActionId(final int actionID) {
        if (this.packet != null) {
            this.writeInt(0, actionID);
        }
        else {
            this.actionID = actionID;
        }
    }
    
    @Deprecated
    public int getActionParam() {
        return this.getActionData();
    }
    
    @Deprecated
    public void setActionParam(final int actionParam) {
        this.setActionData(actionParam);
    }
    
    public int getActionData() {
        if (this.packet != null) {
            return this.readInt(1);
        }
        return this.actionData;
    }
    
    public void setActionData(final int actionData) {
        if (this.packet != null) {
            this.writeInt(1, actionData);
        }
        else {
            this.actionData = actionData;
        }
    }
    
    public Material getBlockType() {
        if (this.packet != null) {
            return NMSUtils.getMaterialFromNMSBlock(this.readObject(0, NMSUtils.blockClass));
        }
        return this.blockType;
    }
    
    public void setBlockType(final Material blockType) {
        if (this.packet != null) {
            final Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(blockType);
            this.write(NMSUtils.blockClass, 0, nmsBlock);
        }
        else {
            this.blockType = blockType;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final Object nmsBlockPos = NMSUtils.generateNMSBlockPos(this.getBlockPosition());
        final Object nmsBlock = NMSUtils.getNMSBlockFromMaterial(this.getBlockType());
        return WrappedPacketOutBlockAction.packetConstructor.newInstance(nmsBlockPos, nmsBlock, this.getActionId(), this.getActionData());
    }
}
