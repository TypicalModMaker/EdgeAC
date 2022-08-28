package xyz.edge.ac.packetevents.packetwrappers.play.out.entitydestroy;

import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntityDestroy extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static Class<?> INT_COLLECTION_CLASS;
    private static Class<?> INT_LIST_CLASS;
    private static Class<?> INT_ARRAY_LIST_CLASS;
    private static Constructor<?> INT_ARRAY_LIST_CONSTRUCTOR;
    private static Method TO_INT_ARRAY_METHOD;
    private static boolean v_1_17;
    private static boolean v_1_17_1;
    private static Constructor<?> packetConstructor;
    private int[] entityIds;
    
    public WrappedPacketOutEntityDestroy(final NMSPacket packet) {
        super(packet);
        this.entityIds = new int[0];
    }
    
    @Deprecated
    public WrappedPacketOutEntityDestroy(final int... entityIds) {
        this.entityIds = new int[0];
        this.entityIds = entityIds;
    }
    
    public WrappedPacketOutEntityDestroy(final int entityID) {
        this.entityIds = new int[0];
        this.setEntityId(entityID);
    }
    
    public WrappedPacketOutEntityDestroy(final Entity entity) {
        this.entityIds = new int[0];
        this.setEntity(entity);
    }
    
    @Override
    protected void load() {
        WrappedPacketOutEntityDestroy.v_1_17_1 = WrappedPacketOutEntityDestroy.version.isNewerThanOrEquals(ServerVersion.v_1_17_1);
        WrappedPacketOutEntityDestroy.v_1_17 = WrappedPacketOutEntityDestroy.version.equals(ServerVersion.v_1_17);
        if (WrappedPacketOutEntityDestroy.v_1_17_1 && WrappedPacketOutEntityDestroy.INT_COLLECTION_CLASS == null) {
            WrappedPacketOutEntityDestroy.INT_COLLECTION_CLASS = Reflection.getClassByNameWithoutException("it.unimi.dsi.fastutil.ints.IntCollection");
            WrappedPacketOutEntityDestroy.INT_LIST_CLASS = Reflection.getClassByNameWithoutException("it.unimi.dsi.fastutil.ints.IntList");
            WrappedPacketOutEntityDestroy.INT_ARRAY_LIST_CLASS = Reflection.getClassByNameWithoutException("it.unimi.dsi.fastutil.ints.IntArrayList");
            try {
                (WrappedPacketOutEntityDestroy.INT_ARRAY_LIST_CONSTRUCTOR = WrappedPacketOutEntityDestroy.INT_ARRAY_LIST_CLASS.getConstructor(int[].class)).setAccessible(true);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                (WrappedPacketOutEntityDestroy.TO_INT_ARRAY_METHOD = WrappedPacketOutEntityDestroy.INT_COLLECTION_CLASS.getDeclaredMethod("toIntArray", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (final NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            if (WrappedPacketOutEntityDestroy.v_1_17) {
                WrappedPacketOutEntityDestroy.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(Integer.TYPE);
            }
            else {
                WrappedPacketOutEntityDestroy.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_DESTROY.getConstructor(int[].class);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int getEntityId() {
        if (this.entityID != -1 || this.entityIds.length > 0 || this.packet == null) {
            if (WrappedPacketOutEntityDestroy.v_1_17) {
                return this.entityID;
            }
            return this.entityIds[0];
        }
        else {
            if (WrappedPacketOutEntityDestroy.v_1_17_1) {
                final Object list = this.readObject(0, WrappedPacketOutEntityDestroy.INT_LIST_CLASS);
                try {
                    this.entityIds = (int[])WrappedPacketOutEntityDestroy.TO_INT_ARRAY_METHOD.invoke(list, new Object[0]);
                }
                catch (final IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return this.entityIds[0];
            }
            if (WrappedPacketOutEntityDestroy.v_1_17) {
                return this.entityID = this.readInt(0);
            }
            this.entityIds = this.readIntArray(0);
            return this.entityIds[0];
        }
    }
    
    @Override
    public void setEntityId(final int entityID) {
        if (this.packet != null) {
            if (WrappedPacketOutEntityDestroy.v_1_17) {
                this.writeInt(0, this.entityID = entityID);
            }
            else {
                this.entityIds = new int[] { entityID };
                if (WrappedPacketOutEntityDestroy.v_1_17_1) {
                    Object intArrayList = null;
                    try {
                        intArrayList = WrappedPacketOutEntityDestroy.INT_ARRAY_LIST_CONSTRUCTOR.newInstance(this.entityIds);
                    }
                    catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    this.write(WrappedPacketOutEntityDestroy.INT_LIST_CLASS, 0, intArrayList);
                }
                else {
                    this.writeIntArray(0, new int[] { this.entityIds[0] });
                }
            }
        }
        else if (WrappedPacketOutEntityDestroy.v_1_17) {
            this.entityID = entityID;
        }
        else {
            this.entityIds = new int[] { entityID };
        }
        this.entity = null;
    }
    
    public int[] getEntityIds() {
        if (this.packet != null) {
            if (WrappedPacketOutEntityDestroy.v_1_17) {
                return new int[] { this.getEntityId() };
            }
            if (WrappedPacketOutEntityDestroy.v_1_17_1) {
                final Object list = this.readObject(0, WrappedPacketOutEntityDestroy.INT_LIST_CLASS);
                try {
                    return (int[])WrappedPacketOutEntityDestroy.TO_INT_ARRAY_METHOD.invoke(list, new Object[0]);
                }
                catch (final IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return this.readIntArray(0);
        }
        else {
            if (WrappedPacketOutEntityDestroy.v_1_17) {
                return new int[] { this.entityID };
            }
            return this.entityIds;
        }
    }
    
    public void setEntityIds(final int... entityIds) {
        if (this.packet != null) {
            if (WrappedPacketOutEntityDestroy.v_1_17) {
                this.setEntityId(entityIds[0]);
            }
            else if (WrappedPacketOutEntityDestroy.v_1_17_1) {
                Object intArrayList = null;
                try {
                    intArrayList = WrappedPacketOutEntityDestroy.INT_ARRAY_LIST_CONSTRUCTOR.newInstance(entityIds);
                }
                catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                this.write(WrappedPacketOutEntityDestroy.INT_LIST_CLASS, 0, intArrayList);
            }
            else {
                this.writeIntArray(0, entityIds);
            }
        }
        else if (WrappedPacketOutEntityDestroy.v_1_17) {
            this.entityID = entityIds[0];
        }
        else {
            this.entityIds = entityIds;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (WrappedPacketOutEntityDestroy.v_1_17) {
            return WrappedPacketOutEntityDestroy.packetConstructor.newInstance(this.getEntityId());
        }
        return WrappedPacketOutEntityDestroy.packetConstructor.newInstance(this.getEntityIds());
    }
}
