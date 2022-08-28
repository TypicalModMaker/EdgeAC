package xyz.edge.ac.packetevents.packetwrappers.play.out.setcooldown;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutSetCooldown extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private Object nmsItem;
    private int cooldownTicks;
    
    public WrappedPacketOutSetCooldown(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutSetCooldown(final Object nmsItem, final int cooldownTicks) {
        this.nmsItem = nmsItem;
        this.cooldownTicks = cooldownTicks;
    }
    
    @Override
    protected void load() {
        try {
            WrappedPacketOutSetCooldown.packetConstructor = PacketTypeClasses.Play.Server.SET_COOLDOWN.getConstructor(NMSUtils.nmsItemClass, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutSetCooldown.version.isNewerThan(ServerVersion.v_1_8_8);
    }
    
    public ItemStack getItemStack() {
        try {
            final Object nmsItem = this.readObject(0, NMSUtils.nmsItemClass);
            final Object nmsItemStack = NMSUtils.itemStackConstructor.newInstance(nmsItem);
            return NMSUtils.toBukkitItemStack(nmsItemStack);
        }
        catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ItemStack(Material.AIR);
        }
    }
    
    public Object getNmsItem() {
        return this.nmsItem;
    }
    
    public void setNMSItemStack(final Object type) {
        if (this.packet != null) {
            this.write(NMSUtils.nmsItemClass, 0, type);
        }
        else {
            this.nmsItem = type;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        return WrappedPacketOutSetCooldown.packetConstructor.newInstance(this.nmsItem, this.getCooldownTicks());
    }
    
    public int getCooldownTicks() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.cooldownTicks;
    }
    
    public void setCooldownTicks(final int cooldownTicks) {
        if (this.packet != null) {
            this.writeInt(0, cooldownTicks);
        }
        else {
            this.cooldownTicks = cooldownTicks;
        }
    }
}
