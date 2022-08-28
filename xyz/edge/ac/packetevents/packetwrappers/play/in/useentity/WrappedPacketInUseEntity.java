package xyz.edge.ac.packetevents.packetwrappers.play.in.useentity;

import xyz.edge.ac.packetevents.utils.player.Hand;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;
import xyz.edge.ac.packetevents.utils.vector.Vector3d;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Method;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public final class WrappedPacketInUseEntity extends WrappedPacketEntityAbstraction
{
    private static Class<? extends Enum<?>> enumEntityUseActionClass;
    private static Class<?> obfuscatedDataInterface;
    private static Class<?> obfuscatedHandContainerClass;
    private static Class<?> obfuscatedTargetAndHandContainerClass;
    private static Method getObfuscatedEntityUseActionMethod;
    private static boolean v_1_7_10;
    private static boolean v_1_9;
    private static boolean v_1_17;
    private EntityUseAction action;
    private Object obfuscatedDataObj;
    
    public WrappedPacketInUseEntity(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        WrappedPacketInUseEntity.v_1_7_10 = WrappedPacketInUseEntity.version.isOlderThan(ServerVersion.v_1_8);
        WrappedPacketInUseEntity.v_1_9 = WrappedPacketInUseEntity.version.isNewerThanOrEquals(ServerVersion.v_1_9);
        WrappedPacketInUseEntity.v_1_17 = WrappedPacketInUseEntity.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            WrappedPacketInUseEntity.enumEntityUseActionClass = NMSUtils.getNMSEnumClass("EnumEntityUseAction");
        }
        catch (final ClassNotFoundException e) {
            if (WrappedPacketInUseEntity.v_1_17) {
                WrappedPacketInUseEntity.enumEntityUseActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, "b");
                WrappedPacketInUseEntity.obfuscatedDataInterface = SubclassUtil.getSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, "EnumEntityUseAction");
                WrappedPacketInUseEntity.obfuscatedHandContainerClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, "d");
                WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, "e");
                WrappedPacketInUseEntity.getObfuscatedEntityUseActionMethod = Reflection.getMethod(WrappedPacketInUseEntity.obfuscatedDataInterface, WrappedPacketInUseEntity.enumEntityUseActionClass, 0);
            }
            else {
                WrappedPacketInUseEntity.enumEntityUseActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Client.USE_ENTITY, "EnumEntityUseAction");
            }
        }
    }
    
    public Optional<Vector3d> getTarget() {
        if (WrappedPacketInUseEntity.v_1_7_10 || this.getAction() != EntityUseAction.INTERACT_AT) {
            return Optional.empty();
        }
        Object vec3DObj;
        if (WrappedPacketInUseEntity.v_1_17) {
            if (this.obfuscatedDataObj == null) {
                this.obfuscatedDataObj = this.readObject(0, WrappedPacketInUseEntity.obfuscatedDataInterface);
            }
            if (!WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.isInstance(this.obfuscatedDataObj)) {
                return Optional.empty();
            }
            final Object obfuscatedTargetAndHandContainerObj = WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.cast(this.obfuscatedDataObj);
            final WrappedPacket wrappedTargetAndHandContainer = new WrappedPacket(new NMSPacket(obfuscatedTargetAndHandContainerObj));
            vec3DObj = wrappedTargetAndHandContainer.readObject(0, NMSUtils.vec3DClass);
        }
        else {
            vec3DObj = this.readObject(0, NMSUtils.vec3DClass);
        }
        final WrappedPacket vec3DWrapper = new WrappedPacket(new NMSPacket(vec3DObj));
        return Optional.of(new Vector3d(vec3DWrapper.readDouble(0), vec3DWrapper.readDouble(1), vec3DWrapper.readDouble(2)));
    }
    
    public void setTarget(final Vector3d target) {
        if (WrappedPacketInUseEntity.v_1_17) {
            final Object vec3DObj = NMSUtils.generateVec3D(target.x, target.y, target.z);
            if (this.obfuscatedDataObj == null) {
                this.obfuscatedDataObj = this.readObject(0, WrappedPacketInUseEntity.obfuscatedDataInterface);
            }
            if (WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.isInstance(this.obfuscatedDataObj)) {
                final Object obfuscatedTargetAndHandContainerObj = WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.cast(this.obfuscatedDataObj);
                final WrappedPacket wrappedTargetAndHandContainer = new WrappedPacket(new NMSPacket(obfuscatedTargetAndHandContainerObj));
                wrappedTargetAndHandContainer.write(NMSUtils.vec3DClass, 0, vec3DObj);
            }
        }
        else if (WrappedPacketInUseEntity.v_1_7_10 && this.getAction() == EntityUseAction.INTERACT_AT) {
            final Object vec3DObj = NMSUtils.generateVec3D(target.x, target.y, target.z);
            this.write(NMSUtils.vec3DClass, 0, vec3DObj);
        }
    }
    
    public EntityUseAction getAction() {
        if (this.action == null) {
            if (WrappedPacketInUseEntity.v_1_17) {
                if (this.obfuscatedDataObj == null) {
                    this.obfuscatedDataObj = this.readObject(0, WrappedPacketInUseEntity.obfuscatedDataInterface);
                }
                try {
                    final Enum<?> useActionEnum = (Enum<?>)WrappedPacketInUseEntity.getObfuscatedEntityUseActionMethod.invoke(this.obfuscatedDataObj, new Object[0]);
                    return this.action = EntityUseAction.values()[useActionEnum.ordinal()];
                }
                catch (final IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            final Enum<?> useActionEnum = this.readEnumConstant(0, WrappedPacketInUseEntity.enumEntityUseActionClass);
            if (useActionEnum == null) {
                return this.action = EntityUseAction.INTERACT;
            }
            return this.action = EntityUseAction.values()[useActionEnum.ordinal()];
        }
        return this.action;
    }
    
    protected void setAction(final EntityUseAction action) {
        this.action = action;
        final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketInUseEntity.enumEntityUseActionClass, action.ordinal());
        if (!WrappedPacketInUseEntity.v_1_17) {
            this.writeEnumConstant(0, enumConst);
        }
    }
    
    public Optional<Hand> getHand() {
        if (!WrappedPacketInUseEntity.v_1_9 || (this.getAction() != EntityUseAction.INTERACT && this.getAction() != EntityUseAction.INTERACT_AT)) {
            return Optional.empty();
        }
        Enum<?> enumHandConst;
        if (WrappedPacketInUseEntity.v_1_17) {
            if (this.obfuscatedDataObj == null) {
                this.obfuscatedDataObj = this.readObject(0, WrappedPacketInUseEntity.obfuscatedDataInterface);
            }
            if (WrappedPacketInUseEntity.obfuscatedHandContainerClass.isInstance(this.obfuscatedDataObj)) {
                final Object obfuscatedHandContainerObj = WrappedPacketInUseEntity.obfuscatedHandContainerClass.cast(this.obfuscatedDataObj);
                final WrappedPacket wrappedHandContainer = new WrappedPacket(new NMSPacket(obfuscatedHandContainerObj));
                enumHandConst = wrappedHandContainer.readEnumConstant(0, NMSUtils.enumHandClass);
            }
            else {
                if (!WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.isInstance(this.obfuscatedDataObj)) {
                    return Optional.empty();
                }
                final Object obfuscatedTargetAndHandContainerObj = WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.cast(this.obfuscatedDataObj);
                final WrappedPacket wrappedTargetAndHandContainer = new WrappedPacket(new NMSPacket(obfuscatedTargetAndHandContainerObj));
                enumHandConst = wrappedTargetAndHandContainer.readEnumConstant(0, NMSUtils.enumHandClass);
            }
        }
        else {
            enumHandConst = this.readEnumConstant(0, NMSUtils.enumHandClass);
        }
        if (enumHandConst == null) {
            return Optional.empty();
        }
        return Optional.of(Hand.values()[enumHandConst.ordinal()]);
    }
    
    public void setHand(final Hand hand) {
        final Enum<?> enumConst = EnumUtil.valueByIndex(NMSUtils.enumHandClass, hand.ordinal());
        if (WrappedPacketInUseEntity.v_1_17) {
            if (this.obfuscatedDataObj == null) {
                this.obfuscatedDataObj = this.readObject(0, WrappedPacketInUseEntity.obfuscatedDataInterface);
            }
            if (WrappedPacketInUseEntity.obfuscatedHandContainerClass.isInstance(this.obfuscatedDataObj)) {
                final Object obfuscatedHandContainerObj = WrappedPacketInUseEntity.obfuscatedHandContainerClass.cast(this.obfuscatedDataObj);
                final WrappedPacket wrappedHandContainer = new WrappedPacket(new NMSPacket(obfuscatedHandContainerObj));
                wrappedHandContainer.writeEnumConstant(0, enumConst);
            }
            else if (WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.isInstance(this.obfuscatedDataObj)) {
                final Object obfuscatedTargetAndHandContainerObj = WrappedPacketInUseEntity.obfuscatedTargetAndHandContainerClass.cast(this.obfuscatedDataObj);
                final WrappedPacket wrappedTargetAndHandContainer = new WrappedPacket(new NMSPacket(obfuscatedTargetAndHandContainerObj));
                wrappedTargetAndHandContainer.writeEnumConstant(0, enumConst);
            }
        }
        else if (WrappedPacketInUseEntity.v_1_9 && (this.getAction() == EntityUseAction.INTERACT || this.getAction() == EntityUseAction.INTERACT_AT)) {
            this.writeEnumConstant(0, enumConst);
        }
    }
    
    public enum EntityUseAction
    {
        INTERACT, 
        ATTACK, 
        INTERACT_AT;
    }
}
