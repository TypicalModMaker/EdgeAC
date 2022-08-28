package xyz.edge.ac.packetevents.packetwrappers.play.in.windowclick;

import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInWindowClick extends WrappedPacket
{
    private static boolean legacy;
    private static boolean v_1_17;
    private static Class<? extends Enum<?>> invClickTypeClass;
    
    public WrappedPacketInWindowClick(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInWindowClick.legacy = WrappedPacketInWindowClick.version.isOlderThanOrEquals(ServerVersion.v_1_8_8);
        WrappedPacketInWindowClick.v_1_17 = WrappedPacketInWindowClick.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketInWindowClick.invClickTypeClass = NMSUtils.getNMSEnumClassWithoutException("InventoryClickType");
        if (WrappedPacketInWindowClick.invClickTypeClass == null) {
            WrappedPacketInWindowClick.invClickTypeClass = NMSUtils.getNMEnumClassWithoutException("world.inventory.InventoryClickType");
        }
    }
    
    public int getWindowId() {
        return this.readInt(WrappedPacketInWindowClick.v_1_17 ? 1 : 0);
    }
    
    public void setWindowId(final int windowID) {
        this.writeInt(WrappedPacketInWindowClick.v_1_17 ? 1 : 0, windowID);
    }
    
    public int getWindowSlot() {
        return this.readInt(WrappedPacketInWindowClick.v_1_17 ? 3 : 1);
    }
    
    public void setWindowSlot(final int slot) {
        this.writeInt(WrappedPacketInWindowClick.v_1_17 ? 3 : 1, slot);
    }
    
    public int getWindowButton() {
        return this.readInt(WrappedPacketInWindowClick.v_1_17 ? 4 : 2);
    }
    
    public void setWindowButton(final int button) {
        this.writeInt(WrappedPacketInWindowClick.v_1_17 ? 4 : 2, button);
    }
    
    public int getActionNumber() {
        if (WrappedPacketInWindowClick.v_1_17) {
            return this.readInt(2);
        }
        return this.readShort(0);
    }
    
    public void setActionNumber(final int actionNumber) {
        if (WrappedPacketInWindowClick.v_1_17) {
            this.writeInt(2, actionNumber);
        }
        else {
            this.writeShort(0, (short)actionNumber);
        }
    }
    
    public int getMode() {
        if (WrappedPacketInWindowClick.legacy) {
            return this.readInt(3);
        }
        final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketInWindowClick.invClickTypeClass);
        return enumConst.ordinal();
    }
    
    public void setMode(final int mode) {
        if (WrappedPacketInWindowClick.legacy) {
            this.writeInt(3, mode);
        }
        else {
            final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketInWindowClick.invClickTypeClass, mode);
            this.writeEnumConstant(0, enumConst);
        }
    }
    
    public ItemStack getClickedItemStack() {
        return this.readItemStack(0);
    }
    
    public void setClickedItemStack(final ItemStack stack) {
        this.writeItemStack(0, stack);
    }
}
