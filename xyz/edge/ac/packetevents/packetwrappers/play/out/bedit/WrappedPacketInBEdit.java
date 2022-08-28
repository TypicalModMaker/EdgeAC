package xyz.edge.ac.packetevents.packetwrappers.play.out.bedit;

import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.player.Hand;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketInBEdit extends WrappedPacket
{
    private static boolean v_1_13;
    private static boolean v_1_17;
    
    public WrappedPacketInBEdit(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInBEdit.v_1_13 = WrappedPacketInBEdit.version.isNewerThanOrEquals(ServerVersion.v_1_13);
        WrappedPacketInBEdit.v_1_17 = WrappedPacketInBEdit.version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }
    
    public ItemStack getItemStack() {
        return this.readItemStack(0);
    }
    
    public void setItemStack(final ItemStack itemStack) {
        this.writeItemStack(0, itemStack);
    }
    
    public boolean isSigning() {
        return this.readBoolean(0);
    }
    
    public void setSigning(final boolean signing) {
        this.writeBoolean(0, signing);
    }
    
    @SupportedVersions(ranges = { ServerVersion.v_1_13, ServerVersion.ERROR })
    public Optional<Hand> getHand() {
        if (WrappedPacketInBEdit.v_1_17) {
            return Optional.of(Hand.values()[this.readInt(0)]);
        }
        if (WrappedPacketInBEdit.v_1_13) {
            final Enum<?> enumConst = this.readEnumConstant(0, NMSUtils.enumHandClass);
            return Optional.of(Hand.valueOf(enumConst.name()));
        }
        return Optional.empty();
    }
    
    @SupportedVersions(ranges = { ServerVersion.v_1_13, ServerVersion.ERROR })
    public void setHand(final Hand hand) {
        if (WrappedPacketInBEdit.v_1_17) {
            this.writeInt(0, hand.ordinal());
        }
        else if (WrappedPacketInBEdit.v_1_13) {
            final Enum<?> enumConst = EnumUtil.valueOf(NMSUtils.enumHandClass, hand.name());
            this.writeEnumConstant(0, enumConst);
        }
    }
}
