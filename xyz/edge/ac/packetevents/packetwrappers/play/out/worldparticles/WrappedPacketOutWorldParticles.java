package xyz.edge.ac.packetevents.packetwrappers.play.out.worldparticles;

import java.util.Optional;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Method;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutWorldParticles extends WrappedPacket
{
    private static Class<? extends Enum<?>> particleEnumClass;
    private static Class<?> particleParamClass;
    private static Method particleParamGetNameMethod;
    private String particleName;
    private boolean longDistance;
    private float x;
    private float y;
    private float z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float particleData;
    private int particleCount;
    private int[] data;
    
    public WrappedPacketOutWorldParticles(final NMSPacket packet) {
        super(packet);
    }
    
    @Override
    protected void load() {
        if (WrappedPacketOutWorldParticles.version.isNewerThan(ServerVersion.v_1_7_10)) {
            try {
                WrappedPacketOutWorldParticles.particleEnumClass = NMSUtils.getNMSEnumClass("EnumParticle");
            }
            catch (final ClassNotFoundException e) {
                WrappedPacketOutWorldParticles.particleParamClass = NMSUtils.getNMSClassWithoutException("ParticleParam");
                if (WrappedPacketOutWorldParticles.particleParamClass == null) {
                    WrappedPacketOutWorldParticles.particleParamClass = NMSUtils.getNMClassWithoutException("core.particles.ParticleParam");
                }
                WrappedPacketOutWorldParticles.particleParamGetNameMethod = Reflection.getMethod(WrappedPacketOutWorldParticles.particleParamClass, String.class, 0);
            }
        }
    }
    
    protected String getParticleName() {
        if (this.packet == null) {
            return this.particleName;
        }
        if (WrappedPacketOutWorldParticles.version.isNewerThan(ServerVersion.v_1_12_1)) {
            final Object particleParamObj = this.readObject(0, WrappedPacketOutWorldParticles.particleParamClass);
            String particleParamName = null;
            try {
                particleParamName = (String)WrappedPacketOutWorldParticles.particleParamGetNameMethod.invoke(particleParamObj, new Object[0]);
            }
            catch (final IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return particleParamName;
        }
        if (WrappedPacketOutWorldParticles.version.isNewerThan(ServerVersion.v_1_7_10)) {
            final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketOutWorldParticles.particleEnumClass);
            return enumConst.name();
        }
        return this.readString(0);
    }
    
    public float getX() {
        if (this.packet != null) {
            return this.readFloat(0);
        }
        return this.x;
    }
    
    public void setX(final float x) {
        if (this.packet != null) {
            this.writeFloat(0, x);
        }
        else {
            this.x = x;
        }
    }
    
    public float getY() {
        if (this.packet != null) {
            return this.readFloat(1);
        }
        return this.y;
    }
    
    public void setY(final float y) {
        if (this.packet != null) {
            this.writeFloat(1, y);
        }
        else {
            this.y = y;
        }
    }
    
    public float getZ() {
        if (this.packet != null) {
            return this.readFloat(2);
        }
        return this.z;
    }
    
    public void setZ(final float z) {
        if (this.packet != null) {
            this.writeFloat(2, z);
        }
        else {
            this.z = z;
        }
    }
    
    public float getOffsetX() {
        if (this.packet != null) {
            return this.readFloat(3);
        }
        return this.offsetX;
    }
    
    public void setOffsetX(final float offsetX) {
        if (this.packet != null) {
            this.writeFloat(3, offsetX);
        }
        else {
            this.offsetX = offsetX;
        }
    }
    
    public float getOffsetY() {
        if (this.packet != null) {
            return this.readFloat(4);
        }
        return this.offsetY;
    }
    
    public void setOffsetY(final float offsetY) {
        if (this.packet != null) {
            this.writeFloat(4, offsetY);
        }
        else {
            this.offsetY = offsetY;
        }
    }
    
    public float getOffsetZ() {
        if (this.packet != null) {
            return this.readFloat(5);
        }
        return this.offsetZ;
    }
    
    public void setOffsetZ(final float offsetZ) {
        if (this.packet != null) {
            this.writeFloat(5, offsetZ);
        }
        else {
            this.offsetZ = offsetZ;
        }
    }
    
    public float getParticleData() {
        if (this.packet != null) {
            return this.readFloat(6);
        }
        return this.particleData;
    }
    
    public void setParticleData(final float particleData) {
        if (this.packet != null) {
            this.writeFloat(6, particleData);
        }
        else {
            this.particleData = particleData;
        }
    }
    
    public int getParticleCount() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.particleCount;
    }
    
    public void setParticleCount(final int particleCount) {
        if (this.packet != null) {
            this.writeInt(0, particleCount);
        }
        else {
            this.particleCount = particleCount;
        }
    }
    
    public Optional<int[]> getData() {
        if (WrappedPacketOutWorldParticles.version.isOlderThan(ServerVersion.v_1_8)) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readIntArray(0));
        }
        return Optional.of(this.data);
    }
    
    public void setData(final int[] data) {
        if (WrappedPacketOutWorldParticles.version.isOlderThan(ServerVersion.v_1_8)) {
            return;
        }
        if (this.packet != null) {
            this.writeIntArray(0, data);
        }
        else {
            this.data = data;
        }
    }
    
    public Optional<Boolean> isLongDistance() {
        if (WrappedPacketOutWorldParticles.version.isOlderThan(ServerVersion.v_1_8)) {
            return Optional.empty();
        }
        if (this.packet != null) {
            return Optional.of(this.readBoolean(0));
        }
        return Optional.of(this.longDistance);
    }
    
    public void setLongDistance(final boolean longDistance) {
        if (WrappedPacketOutWorldParticles.version.isOlderThan(ServerVersion.v_1_8)) {
            return;
        }
        if (this.packet != null) {
            this.writeBoolean(0, longDistance);
        }
        else {
            this.longDistance = longDistance;
        }
    }
}
