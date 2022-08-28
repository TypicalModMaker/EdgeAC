package xyz.edge.ac.packetevents.packetwrappers.play.out.entitymetadata;

import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.google.OptionalUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedWatchableObject extends WrappedPacket
{
    private static int valueIndex;
    private static Class<?> googleOptionalClass;
    
    public WrappedWatchableObject(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        if (WrappedWatchableObject.version.isNewerThan(ServerVersion.v_1_8_8)) {
            WrappedWatchableObject.valueIndex = 1;
            try {
                WrappedWatchableObject.googleOptionalClass = Class.forName("com.google.common.base.Optional");
            }
            catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    public int getIndex() {
        if (WrappedWatchableObject.version.isNewerThan(ServerVersion.v_1_8_8)) {
            final Object dataWatcherObject = this.readAnyObject(0);
            final WrappedPacket wrappedDataWatcher = new WrappedPacket(new NMSPacket(dataWatcherObject));
            return wrappedDataWatcher.readInt(0);
        }
        return this.readInt(0);
    }
    
    public void setIndex(final int index) {
        if (WrappedWatchableObject.version.isNewerThan(ServerVersion.v_1_8_8)) {
            final Object dataWatcherObject = this.readAnyObject(0);
            final WrappedPacket wrappedDataWatcher = new WrappedPacket(new NMSPacket(dataWatcherObject));
            wrappedDataWatcher.writeInt(0, index);
        }
        else {
            this.writeInt(0, index);
        }
    }
    
    public boolean isDirty() {
        return this.readBoolean(0);
    }
    
    public void setDirty(final boolean dirty) {
        this.writeBoolean(0, dirty);
    }
    
    public Object getRawValue() {
        return this.readAnyObject(WrappedWatchableObject.valueIndex);
    }
    
    public void setRawValue(final Object rawValue) {
        this.writeAnyObject(WrappedWatchableObject.valueIndex, rawValue);
    }
    
    protected Object getValue() {
        final Object rawValue = this.getRawValue();
        final Class<?> rawType = rawValue.getClass();
        if (rawType.equals(WrappedWatchableObject.googleOptionalClass)) {
            return OptionalUtils.convertToJavaOptional(rawValue);
        }
        if (rawType.equals(NMSUtils.iChatBaseComponentClass)) {
            return rawValue;
        }
        if (rawType.equals(NMSUtils.nmsItemStackClass)) {
            return NMSUtils.toBukkitItemStack(rawValue);
        }
        return rawValue;
    }
    
    protected void setValue(final Object value) {
    }
    
    static {
        WrappedWatchableObject.valueIndex = 2;
    }
}
