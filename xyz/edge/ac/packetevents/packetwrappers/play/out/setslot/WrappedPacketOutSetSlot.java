package xyz.edge.ac.packetevents.packetwrappers.play.out.setslot;

import java.util.Optional;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.inventory.ItemStack;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutSetSlot extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_17;
    private static boolean v_1_17_1;
    private static Constructor<?> packetConstructor;
    private int windowID;
    private int stateID;
    private int slot;
    private ItemStack itemStack;
    
    public WrappedPacketOutSetSlot(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutSetSlot(final int windowID, final int slot, final ItemStack itemstack) {
        this.windowID = windowID;
        this.stateID = 0;
        this.slot = slot;
        this.itemStack = itemstack;
    }
    
    public WrappedPacketOutSetSlot(final int windowID, final int stateID, final int slot, final ItemStack itemstack) {
        this.windowID = windowID;
        this.stateID = stateID;
        this.slot = slot;
        this.itemStack = itemstack;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutSetSlot.v_1_17 = WrappedPacketOutSetSlot.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutSetSlot.v_1_17_1 = WrappedPacketOutSetSlot.version.isNewerThanOrEquals(ServerVersion.v_1_17_1);
        try {
            if (WrappedPacketOutSetSlot.v_1_17_1) {
                WrappedPacketOutSetSlot.packetConstructor = PacketTypeClasses.Play.Server.SET_SLOT.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, NMSUtils.nmsItemStackClass);
            }
            else {
                WrappedPacketOutSetSlot.packetConstructor = PacketTypeClasses.Play.Server.SET_SLOT.getConstructor(Integer.TYPE, Integer.TYPE, NMSUtils.nmsItemStackClass);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getWindowId() {
        if (this.packet != null) {
            final int index = WrappedPacketOutSetSlot.v_1_17 ? 2 : 0;
            return this.readInt(index);
        }
        return this.windowID;
    }
    
    public void setWindowId(final int windowID) {
        if (this.packet != null) {
            final int index = WrappedPacketOutSetSlot.v_1_17 ? 2 : 0;
            this.writeInt(index, windowID);
        }
        else {
            this.windowID = windowID;
        }
    }
    
    public Optional<Integer> getStateId() {
        if (!WrappedPacketOutSetSlot.v_1_17_1) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readInt(4));
        }
        return Optional.of(this.stateID);
    }
    
    public void setStateId(final int stateID) {
        if (WrappedPacketOutSetSlot.v_1_17_1) {
            if (this.packet != null) {
                this.writeInt(4, stateID);
            }
            else {
                this.stateID = stateID;
            }
        }
    }
    
    public int getSlot() {
        if (this.packet != null) {
            final int index = WrappedPacketOutSetSlot.v_1_17_1 ? 4 : (WrappedPacketOutSetSlot.v_1_17 ? 3 : 1);
            return this.readInt(index);
        }
        return this.slot;
    }
    
    public void setSlot(final int slot) {
        if (this.packet != null) {
            final int index = WrappedPacketOutSetSlot.v_1_17_1 ? 4 : (WrappedPacketOutSetSlot.v_1_17 ? 3 : 1);
            this.writeInt(index, slot);
        }
        else {
            this.slot = slot;
        }
    }
    
    public ItemStack getItemStack() {
        if (this.packet != null) {
            return this.readItemStack(0);
        }
        return this.itemStack;
    }
    
    public void setItemStack(final ItemStack itemStack) {
        if (this.packet != null) {
            this.writeItemStack(0, itemStack);
        }
        else {
            this.itemStack = itemStack;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (WrappedPacketOutSetSlot.v_1_17_1) {
            return WrappedPacketOutSetSlot.packetConstructor.newInstance(this.getWindowId(), this.getStateId().get(), this.getSlot(), NMSUtils.toNMSItemStack(this.getItemStack()));
        }
        return WrappedPacketOutSetSlot.packetConstructor.newInstance(this.getWindowId(), this.getSlot(), NMSUtils.toNMSItemStack(this.getItemStack()));
    }
}
