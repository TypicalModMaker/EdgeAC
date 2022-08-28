package xyz.edge.ac.packetevents.packetwrappers.play.out.gamestatechange;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutGameStateChange extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> packetConstructor;
    private static Constructor<?> reasonClassConstructor;
    private static Class<?> reasonClassType;
    private static boolean reasonIntMode;
    private static boolean valueFloatMode;
    private int reason;
    private double value;
    
    public WrappedPacketOutGameStateChange(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutGameStateChange(final int reason, final double value) {
        this.reason = reason;
        this.value = value;
    }
    
    public WrappedPacketOutGameStateChange(final int reason, final float value) {
        this(reason, (double)value);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutGameStateChange.reasonClassType = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, 0);
        if (WrappedPacketOutGameStateChange.reasonClassType != null) {
            try {
                WrappedPacketOutGameStateChange.reasonClassConstructor = WrappedPacketOutGameStateChange.reasonClassType.getConstructor(Integer.TYPE);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        WrappedPacketOutGameStateChange.reasonIntMode = (WrappedPacketOutGameStateChange.reasonClassType == null);
        if (WrappedPacketOutGameStateChange.reasonIntMode) {
            WrappedPacketOutGameStateChange.reasonClassType = Integer.TYPE;
        }
        WrappedPacketOutGameStateChange.valueFloatMode = (Reflection.getField(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, Double.TYPE, 0) == null);
        try {
            Class<?> valueClassType;
            if (WrappedPacketOutGameStateChange.valueFloatMode) {
                valueClassType = Float.TYPE;
            }
            else {
                valueClassType = Double.TYPE;
            }
            WrappedPacketOutGameStateChange.packetConstructor = PacketTypeClasses.Play.Server.GAME_STATE_CHANGE.getConstructor(WrappedPacketOutGameStateChange.reasonClassType, valueClassType);
        }
        catch (final NullPointerException e2) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "PacketEvents failed to find the constructor for the outbound Game state packet wrapper.");
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public int getReason() {
        if (this.packet == null) {
            return this.reason;
        }
        if (WrappedPacketOutGameStateChange.reasonIntMode) {
            return this.readInt(0);
        }
        final Object reasonObject = this.readObject(12, WrappedPacketOutGameStateChange.reasonClassType);
        final WrappedPacket reasonObjWrapper = new WrappedPacket(new NMSPacket(reasonObject));
        return reasonObjWrapper.readInt(0);
    }
    
    public void setReason(final int reason) {
        if (this.packet != null) {
            if (WrappedPacketOutGameStateChange.reasonIntMode) {
                this.writeInt(0, reason);
            }
            else {
                final Object reasonObj = this.readObject(12, WrappedPacketOutGameStateChange.reasonClassType);
                final WrappedPacket reasonObjWrapper = new WrappedPacket(new NMSPacket(reasonObj));
                reasonObjWrapper.writeInt(0, reason);
            }
        }
        else {
            this.reason = reason;
        }
    }
    
    public double getValue() {
        if (this.packet == null) {
            return this.value;
        }
        if (WrappedPacketOutGameStateChange.valueFloatMode) {
            return this.readFloat(0);
        }
        return this.readDouble(0);
    }
    
    public void setValue(final double value) {
        if (this.packet != null) {
            if (WrappedPacketOutGameStateChange.valueFloatMode) {
                this.writeFloat(0, (float)value);
            }
            else {
                this.writeDouble(0, value);
            }
        }
        else {
            this.value = value;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (WrappedPacketOutGameStateChange.reasonIntMode) {
            if (WrappedPacketOutGameStateChange.valueFloatMode) {
                return WrappedPacketOutGameStateChange.packetConstructor.newInstance(this.getReason(), (float)this.getValue());
            }
            return WrappedPacketOutGameStateChange.packetConstructor.newInstance(this.getReason(), this.getValue());
        }
        else {
            final Object reasonObject = WrappedPacketOutGameStateChange.reasonClassConstructor.newInstance(this.getReason());
            if (WrappedPacketOutGameStateChange.valueFloatMode) {
                return WrappedPacketOutGameStateChange.packetConstructor.newInstance(reasonObject, (float)this.getValue());
            }
            return WrappedPacketOutGameStateChange.packetConstructor.newInstance(reasonObject, this.getValue());
        }
    }
}
