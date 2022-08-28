package xyz.edge.ac.packetevents.packetwrappers.play.out.windowitems;

import java.util.Optional;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import java.util.ArrayList;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutWindowItems extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_17;
    private static boolean v_1_17_1;
    private static Object nonNullListInstance;
    private static Class<?> nonNullListClass;
    private static Constructor<?> packetConstructor;
    private int windowID;
    private int stateID;
    private List<ItemStack> slotData;
    private ItemStack heldItem;
    
    public WrappedPacketOutWindowItems(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutWindowItems(final int windowID, final int stateID, final List<ItemStack> slots, final ItemStack heldItem) {
        this.windowID = windowID;
        this.stateID = stateID;
        this.slotData = slots;
        this.heldItem = heldItem;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutWindowItems.v_1_17 = WrappedPacketOutWindowItems.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutWindowItems.v_1_17_1 = WrappedPacketOutWindowItems.version.isNewerThanOrEquals(ServerVersion.v_1_17_1);
        try {
            if (WrappedPacketOutWindowItems.v_1_17) {
                WrappedPacketOutWindowItems.nonNullListClass = NMSUtils.getNMClassWithoutException("core.NonNullList");
                if (WrappedPacketOutWindowItems.v_1_17_1) {
                    final Constructor<?> nonNullListConstructor = WrappedPacketOutWindowItems.nonNullListClass.getDeclaredConstructors()[0];
                    nonNullListConstructor.setAccessible(true);
                    WrappedPacketOutWindowItems.nonNullListInstance = nonNullListConstructor.newInstance(new ArrayList(), null);
                    WrappedPacketOutWindowItems.packetConstructor = PacketTypeClasses.Play.Server.WINDOW_ITEMS.getConstructor(Integer.TYPE, Integer.TYPE, WrappedPacketOutWindowItems.nonNullListClass, NMSUtils.nmsItemStackClass);
                }
                else {
                    final Constructor<?> nonNullListConstructor = WrappedPacketOutWindowItems.nonNullListClass.getDeclaredConstructor((Class<?>[])new Class[0]);
                    nonNullListConstructor.setAccessible(true);
                    WrappedPacketOutWindowItems.nonNullListInstance = nonNullListConstructor.newInstance(new Object[0]);
                    WrappedPacketOutWindowItems.packetConstructor = PacketTypeClasses.Play.Server.WINDOW_ITEMS.getConstructor(Integer.TYPE, WrappedPacketOutWindowItems.nonNullListClass);
                }
            }
            else {
                WrappedPacketOutWindowItems.packetConstructor = PacketTypeClasses.Play.Server.WINDOW_ITEMS.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
    
    public int getWindowId() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.windowID;
    }
    
    public void setWindowId(final int windowID) {
        if (this.packet != null) {
            this.writeInt(0, windowID);
        }
        else {
            this.windowID = windowID;
        }
    }
    
    public int getStateId() {
        if (this.packet != null) {
            return this.readInt(1);
        }
        return this.stateID;
    }
    
    public void setStateId(final int stateID) {
        if (this.packet != null) {
            this.writeInt(1, stateID);
        }
        else {
            this.stateID = stateID;
        }
    }
    
    public List<ItemStack> getSlots() {
        if (this.packet != null) {
            final List<ItemStack> slots = new ArrayList<ItemStack>();
            if (WrappedPacketOutWindowItems.version.isNewerThan(ServerVersion.v_1_10_2)) {
                final List<Object> nmsItemStacks = this.readList(0);
                for (final Object nmsItemStack : nmsItemStacks) {
                    slots.add(NMSUtils.toBukkitItemStack(nmsItemStack));
                }
            }
            else {
                final Object[] array;
                final Object[] nmsItemStacks2 = array = (Object[])this.readAnyObject(1);
                for (final Object nmsItemStack2 : array) {
                    slots.add(NMSUtils.toBukkitItemStack(nmsItemStack2));
                }
            }
            return slots;
        }
        return this.slotData;
    }
    
    public void setSlots(final List<ItemStack> slots) {
        if (this.packet != null) {
            if (WrappedPacketOutWindowItems.version.isNewerThan(ServerVersion.v_1_10_2)) {
                final List<Object> nmsItemStacks = new ArrayList<Object>();
                for (final ItemStack itemStack : slots) {
                    nmsItemStacks.add(NMSUtils.toNMSItemStack(itemStack));
                }
                this.writeList(0, nmsItemStacks);
            }
            else {
                final Object[] nmsItemStacks2 = new Object[slots.size()];
                for (int i = 0; i < slots.size(); ++i) {
                    nmsItemStacks2[i] = NMSUtils.toNMSItemStack(slots.get(i));
                }
                this.writeAnyObject(1, nmsItemStacks2);
            }
        }
        else {
            this.slotData = slots;
        }
    }
    
    public Optional<ItemStack> getHeldItem() {
        if (!WrappedPacketOutWindowItems.v_1_17_1) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.ofNullable(NMSUtils.toBukkitItemStack(this.readObject(0, NMSUtils.nmsItemStackClass)));
        }
        return Optional.of(this.heldItem);
    }
    
    public void setHeldItem(final ItemStack heldItem) {
        if (this.packet != null) {
            this.writeObject(0, NMSUtils.toNMSItemStack(heldItem));
        }
        else {
            this.heldItem = heldItem;
        }
    }
    
    private Object getNMSItemHeld() {
        if (this.packet != null) {
            return this.readObject(0, NMSUtils.nmsItemStackClass);
        }
        return NMSUtils.toNMSItemStack(this.heldItem);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutWindowItems.v_1_17_1) {
            packetInstance = WrappedPacketOutWindowItems.packetConstructor.newInstance(this.getWindowId(), this.stateID, WrappedPacketOutWindowItems.nonNullListInstance, this.getNMSItemHeld());
            final WrappedPacketOutWindowItems wrappedPacketOutWindowItems = new WrappedPacketOutWindowItems(new NMSPacket(packetInstance));
            wrappedPacketOutWindowItems.setSlots(this.getSlots());
        }
        else if (WrappedPacketOutWindowItems.v_1_17) {
            packetInstance = WrappedPacketOutWindowItems.packetConstructor.newInstance(this.getWindowId(), WrappedPacketOutWindowItems.nonNullListInstance);
            final WrappedPacketOutWindowItems wrappedPacketOutWindowItems = new WrappedPacketOutWindowItems(new NMSPacket(packetInstance));
            wrappedPacketOutWindowItems.setSlots(this.getSlots());
        }
        else {
            packetInstance = WrappedPacketOutWindowItems.packetConstructor.newInstance(new Object[0]);
            final WrappedPacketOutWindowItems wrappedPacketOutWindowItems = new WrappedPacketOutWindowItems(new NMSPacket(packetInstance));
            wrappedPacketOutWindowItems.setWindowId(this.getWindowId());
            wrappedPacketOutWindowItems.setSlots(this.getSlots());
        }
        return packetInstance;
    }
}
