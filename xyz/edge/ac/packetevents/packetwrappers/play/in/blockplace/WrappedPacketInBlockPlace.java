package xyz.edge.ac.packetevents.packetwrappers.play.in.blockplace;

import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.utils.vector.Vector3f;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import xyz.edge.ac.packetevents.utils.player.Direction;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.player.Hand;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketInBlockPlace extends WrappedPacket
{
    private static boolean newerThan_v_1_8_8;
    private static boolean newerThan_v_1_7_10;
    private static int handEnumIndex;
    
    public WrappedPacketInBlockPlace(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInBlockPlace.newerThan_v_1_7_10 = WrappedPacketInBlockPlace.version.isNewerThan(ServerVersion.v_1_7_10);
        WrappedPacketInBlockPlace.newerThan_v_1_8_8 = WrappedPacketInBlockPlace.version.isNewerThan(ServerVersion.v_1_8_8);
        try {
            final Object handEnum = this.readObject(1, (Class<?>)NMSUtils.enumHandClass);
            WrappedPacketInBlockPlace.handEnumIndex = 1;
        }
        catch (final Exception ex) {
            WrappedPacketInBlockPlace.handEnumIndex = 0;
        }
    }
    
    public Hand getHand() {
        if (WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            final Enum<?> enumConst = this.readEnumConstant(WrappedPacketInBlockPlace.handEnumIndex, NMSUtils.enumHandClass);
            return Hand.values()[enumConst.ordinal()];
        }
        return Hand.MAIN_HAND;
    }
    
    public void setHand(final Hand hand) {
        if (WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            final Enum<?> enumConst = EnumUtil.valueByIndex(NMSUtils.enumHandClass, hand.ordinal());
            this.writeEnumConstant(WrappedPacketInBlockPlace.handEnumIndex, enumConst);
        }
    }
    
    public Direction getDirection() {
        if (WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            final WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(new NMSPacket(this.packet.getRawNMSPacket()));
            return blockPlace_1_9.getDirection();
        }
        if (WrappedPacketInBlockPlace.newerThan_v_1_7_10) {
            final WrappedPacketInBlockPlace_1_8 blockPlace_1_10 = new WrappedPacketInBlockPlace_1_8(new NMSPacket(this.packet.getRawNMSPacket()));
            return Direction.getDirection(blockPlace_1_10.getFace());
        }
        final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(new NMSPacket(this.packet.getRawNMSPacket()));
        return Direction.getDirection(blockPlace_1_7_10.getFace());
    }
    
    public void setDirection(final Direction direction) {
        if (WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            final WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(new NMSPacket(this.packet.getRawNMSPacket()));
            blockPlace_1_9.setDirection(direction);
        }
        else if (WrappedPacketInBlockPlace.newerThan_v_1_7_10) {
            final WrappedPacketInBlockPlace_1_8 blockPlace_1_10 = new WrappedPacketInBlockPlace_1_8(new NMSPacket(this.packet.getRawNMSPacket()));
            blockPlace_1_10.setFace(direction.getFaceValue());
        }
        else {
            final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(new NMSPacket(this.packet.getRawNMSPacket()));
            blockPlace_1_7_10.setFace(direction.getFaceValue());
        }
    }
    
    public Vector3i getBlockPosition() {
        Vector3i blockPos;
        if (WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            final WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(this.packet);
            blockPos = blockPlace_1_9.getBlockPosition();
        }
        else if (WrappedPacketInBlockPlace.newerThan_v_1_7_10) {
            final WrappedPacketInBlockPlace_1_8 blockPlace_1_10 = new WrappedPacketInBlockPlace_1_8(this.packet);
            blockPos = blockPlace_1_10.getBlockPosition();
        }
        else {
            final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(this.packet);
            blockPos = blockPlace_1_7_10.getBlockPosition();
        }
        return blockPos;
    }
    
    public Optional<Vector3f> getCursorPosition() {
        if (WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            return Optional.empty();
        }
        if (WrappedPacketInBlockPlace.newerThan_v_1_7_10) {
            final WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(this.packet);
            return Optional.of(blockPlace_1_8.getCursorPosition());
        }
        final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(this.packet);
        return Optional.of(blockPlace_1_7_10.getCursorPosition());
    }
    
    public void setCursorPosition(final Vector3f cursorPos) {
        if (!WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            if (WrappedPacketInBlockPlace.newerThan_v_1_7_10) {
                final WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(this.packet);
                blockPlace_1_8.setCursorPosition(cursorPos);
            }
            else {
                final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(this.packet);
                blockPlace_1_7_10.setCursorPosition(cursorPos);
            }
        }
    }
    
    public Optional<ItemStack> getItemStack() {
        if (WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            return Optional.empty();
        }
        if (WrappedPacketInBlockPlace.newerThan_v_1_7_10) {
            final WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(this.packet);
            return Optional.of(blockPlace_1_8.getItemStack());
        }
        final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(this.packet);
        return Optional.of(blockPlace_1_7_10.getItemStack());
    }
    
    public void setItemStack(final ItemStack stack) {
        if (!WrappedPacketInBlockPlace.newerThan_v_1_8_8) {
            if (WrappedPacketInBlockPlace.newerThan_v_1_7_10) {
                final WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(this.packet);
                blockPlace_1_8.setItemStack(stack);
            }
            else {
                final WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(this.packet);
                blockPlace_1_7_10.setItemStack(stack);
            }
        }
    }
}
