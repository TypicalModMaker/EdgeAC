package xyz.edge.ac.packetevents.packetwrappers;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import xyz.edge.ac.packetevents.utils.MojangEitherUtil;
import xyz.edge.ac.packetevents.utils.ConditionalValue;
import xyz.edge.ac.packetevents.utils.world.Difficulty;
import xyz.edge.ac.packetevents.utils.world.Dimension;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.utils.player.GameMode;
import org.bukkit.inventory.ItemStack;
import java.lang.reflect.InvocationTargetException;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.vector.Vector3i;
import xyz.edge.ac.packetevents.exceptions.WrapperFieldNotFoundException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.reflect.Method;
import xyz.edge.ac.packetevents.utils.reflection.Reflection;
import java.util.List;
import java.util.Arrays;
import java.lang.annotation.Annotation;
import java.util.logging.Level;
import xyz.edge.ac.packetevents.PacketEvents;
import xyz.edge.ac.packetevents.utils.reflection.ClassUtil;
import xyz.edge.ac.packetevents.exceptions.WrapperUnsupportedUsageException;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import java.lang.reflect.Field;
import java.util.Map;
import xyz.edge.ac.packetevents.packetwrappers.api.WrapperPacketWriter;
import xyz.edge.ac.packetevents.packetwrappers.api.WrapperPacketReader;

public class WrappedPacket implements WrapperPacketReader, WrapperPacketWriter
{
    private static final Map<Class<? extends WrappedPacket>, Boolean> LOADED_WRAPPERS;
    private static final Map<Class<?>, Map<Class<?>, Field[]>> FIELD_CACHE;
    private static final Field[] EMPTY_FIELD_ARRAY;
    private static byte isVersion_1_17;
    public static ServerVersion version;
    protected final NMSPacket packet;
    private final Class<?> packetClass;
    
    public WrappedPacket() {
        this.packet = null;
        this.packetClass = null;
        this.load0();
    }
    
    public WrappedPacket(final NMSPacket packet) {
        this(packet, packet.getRawNMSPacket().getClass());
    }
    
    public WrappedPacket(final NMSPacket packet, Class<?> packetClass) {
        if (packetClass.getSuperclass().equals(PacketTypeClasses.Play.Client.FLYING)) {
            packetClass = PacketTypeClasses.Play.Client.FLYING;
        }
        else if (packetClass.getSuperclass().equals(PacketTypeClasses.Play.Server.ENTITY)) {
            packetClass = PacketTypeClasses.Play.Server.ENTITY;
        }
        this.packetClass = packetClass;
        this.packet = packet;
        this.load0();
    }
    
    private void load0() {
        final Class<? extends WrappedPacket> clazz = this.getClass();
        if (!WrappedPacket.LOADED_WRAPPERS.containsKey(clazz)) {
            if (!this.isSupported()) {
                throw new WrapperUnsupportedUsageException(this.getClass());
            }
            try {
                this.load();
            }
            catch (final Exception ex) {
                final String wrapperName = ClassUtil.getClassSimpleName(clazz);
                PacketEvents.get().getPlugin().getLogger().log(Level.SEVERE, "PacketEvents found an exception while loading the " + wrapperName + " packet wrapper. Please report this bug! Tell us about your server version, spigot and code(of you using the wrapper)", ex);
                WrappedPacket.LOADED_WRAPPERS.put(clazz, false);
            }
            WrappedPacket.LOADED_WRAPPERS.put(clazz, true);
        }
    }
    
    protected void load() {
    }
    
    protected boolean hasLoaded() {
        return WrappedPacket.LOADED_WRAPPERS.getOrDefault(this.getClass(), false);
    }
    
    protected void throwUnsupportedOperation(final Enum<?> enumConst) throws UnsupportedOperationException {
        final Class<?> enumConstClass = enumConst.getClass();
        Field field = null;
        try {
            field = enumConstClass.getField(enumConst.name());
        }
        catch (final NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field.isAnnotationPresent(SupportedVersions.class)) {
            final SupportedVersions supportedVersions = field.getAnnotation(SupportedVersions.class);
            final List<ServerVersion> versionList = this.parseSupportedVersionsAnnotation(supportedVersions);
            final String supportedVersionsMsg = Arrays.toString(versionList.toArray(new ServerVersion[0]));
            throw new UnsupportedOperationException("PacketEvents failed to use the " + enumConst.name() + " enum constant in the " + enumConstClass.getSimpleName() + " enum. This enum constant is not supported on your server version. (" + PacketEvents.get().getServerUtils().getVersion() + ")\n This enum constant is only supported on these server versions: " + supportedVersionsMsg);
        }
        throw new UnsupportedOperationException("PacketEvents failed to use the " + enumConst.name() + " enum constant in the " + enumConstClass.getSimpleName() + " enum. This enum constant is not supported on your server version. (" + PacketEvents.get().getServerUtils().getVersion() + ")\n Failed to find out what server versions this enum constant is supported on.");
    }
    
    protected void throwUnsupportedOperation() throws UnsupportedOperationException {
        final String currentMethodName = "throwUnsupportedOperation";
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int stackTraceElementIndex = 2;
        for (int i = 0; i < stackTraceElements.length; ++i) {
            final StackTraceElement element = stackTraceElements[i];
            if (element.getMethodName().equals("throwUnsupportedOperation")) {
                stackTraceElementIndex = i + 1;
                break;
            }
        }
        final StackTraceElement stackTraceElement = stackTraceElements[stackTraceElementIndex];
        final String methodName = stackTraceElement.getMethodName();
        final List<Method> possibleMethods = Reflection.getMethods(this.getClass(), methodName, null);
        Method method = null;
        for (final Method m : possibleMethods) {
            if (m.isAnnotationPresent(SupportedVersions.class)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new UnsupportedOperationException("PacketEvents failed to access your requested field. This field is not supported on your server version. Failed to lookup the server versions this field supports...");
        }
        final SupportedVersions supportedVersions = method.getAnnotation(SupportedVersions.class);
        final List<ServerVersion> versionList = this.parseSupportedVersionsAnnotation(supportedVersions);
        final String supportedVersionsMsg = Arrays.toString(versionList.toArray(new ServerVersion[0]));
        throw new UnsupportedOperationException("PacketEvents failed to access your requested field. This field is not supported on your server version. (" + PacketEvents.get().getServerUtils().getVersion() + ")\n This field is only supported on these server versions: " + supportedVersionsMsg);
    }
    
    private List<ServerVersion> parseSupportedVersionsAnnotation(final SupportedVersions supportedVersions) {
        final List<ServerVersion> versionList = new ArrayList<ServerVersion>();
        for (int i = 0; i < supportedVersions.ranges().length; i += 2) {
            ServerVersion first = supportedVersions.ranges()[i];
            ServerVersion last = supportedVersions.ranges()[i + 1];
            if (first == last) {
                versionList.add(first);
            }
            else {
                if (first == ServerVersion.ERROR) {
                    first = ServerVersion.getOldest();
                }
                else if (last == ServerVersion.ERROR) {
                    last = ServerVersion.getLatest();
                }
                versionList.addAll(Arrays.asList(ServerVersion.values()).subList(first.ordinal(), last.ordinal() + 1));
            }
        }
        versionList.remove(ServerVersion.ERROR);
        return versionList;
    }
    
    @Override
    public boolean readBoolean(final int index) {
        return this.read(index, (Class<? extends Boolean>)Boolean.TYPE);
    }
    
    @Override
    public byte readByte(final int index) {
        return this.read(index, (Class<? extends Byte>)Byte.TYPE);
    }
    
    @Override
    public short readShort(final int index) {
        return this.read(index, (Class<? extends Short>)Short.TYPE);
    }
    
    @Override
    public int readInt(final int index) {
        return this.read(index, (Class<? extends Integer>)Integer.TYPE);
    }
    
    @Override
    public long readLong(final int index) {
        return this.read(index, (Class<? extends Long>)Long.TYPE);
    }
    
    @Override
    public float readFloat(final int index) {
        return this.read(index, (Class<? extends Float>)Float.TYPE);
    }
    
    @Override
    public double readDouble(final int index) {
        return this.read(index, (Class<? extends Double>)Double.TYPE);
    }
    
    @Override
    public boolean[] readBooleanArray(final int index) {
        return this.read(index, (Class<? extends boolean[]>)boolean[].class);
    }
    
    @Override
    public byte[] readByteArray(final int index) {
        return this.read(index, (Class<? extends byte[]>)byte[].class);
    }
    
    @Override
    public short[] readShortArray(final int index) {
        return this.read(index, (Class<? extends short[]>)short[].class);
    }
    
    @Override
    public int[] readIntArray(final int index) {
        return this.read(index, (Class<? extends int[]>)int[].class);
    }
    
    @Override
    public long[] readLongArray(final int index) {
        return this.read(index, (Class<? extends long[]>)long[].class);
    }
    
    @Override
    public float[] readFloatArray(final int index) {
        return this.read(index, (Class<? extends float[]>)float[].class);
    }
    
    @Override
    public double[] readDoubleArray(final int index) {
        return this.read(index, (Class<? extends double[]>)double[].class);
    }
    
    @Override
    public String[] readStringArray(final int index) {
        return this.read(index, (Class<? extends String[]>)String[].class);
    }
    
    @Override
    public String readString(final int index) {
        return this.read(index, (Class<? extends String>)String.class);
    }
    
    @Override
    public Object readAnyObject(final int index) {
        try {
            final Field f = this.packetClass.getDeclaredFields()[index];
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            try {
                return f.get(this.packet.getRawNMSPacket());
            }
            catch (final IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        catch (final ArrayIndexOutOfBoundsException e2) {
            throw new WrapperFieldNotFoundException("PacketEvents failed to find any field indexed " + index + " in the " + ClassUtil.getClassSimpleName(this.packetClass) + " class!");
        }
        return null;
    }
    
    @Override
    public <T> T readObject(final int index, final Class<? extends T> type) {
        return (T)this.read(index, (Class<?>)type);
    }
    
    @Override
    public Enum<?> readEnumConstant(final int index, final Class<? extends Enum<?>> type) {
        return this.read(index, type);
    }
    
    public <T> T read(final int index, final Class<? extends T> type) {
        try {
            final Field field = this.getField(type, index);
            return (T)field.get(this.packet.getRawNMSPacket());
        }
        catch (final IllegalAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            throw new WrapperFieldNotFoundException(this.packetClass, type, index);
        }
    }
    
    @Override
    public void writeBoolean(final int index, final boolean value) {
        this.write(Boolean.TYPE, index, value);
    }
    
    @Override
    public void writeByte(final int index, final byte value) {
        this.write(Byte.TYPE, index, value);
    }
    
    @Override
    public void writeShort(final int index, final short value) {
        this.write(Short.TYPE, index, value);
    }
    
    @Override
    public void writeInt(final int index, final int value) {
        this.write(Integer.TYPE, index, value);
    }
    
    @Override
    public void writeLong(final int index, final long value) {
        this.write(Long.TYPE, index, value);
    }
    
    @Override
    public void writeFloat(final int index, final float value) {
        this.write(Float.TYPE, index, value);
    }
    
    @Override
    public void writeDouble(final int index, final double value) {
        this.write(Double.TYPE, index, value);
    }
    
    @Override
    public void writeString(final int index, final String value) {
        this.write(String.class, index, value);
    }
    
    @Override
    public void writeObject(final int index, final Object value) {
        this.write(value.getClass(), index, value);
    }
    
    @Override
    public void writeBooleanArray(final int index, final boolean[] array) {
        this.write(boolean[].class, index, array);
    }
    
    @Override
    public void writeByteArray(final int index, final byte[] value) {
        this.write(byte[].class, index, value);
    }
    
    @Override
    public void writeShortArray(final int index, final short[] value) {
        this.write(short[].class, index, value);
    }
    
    @Override
    public void writeIntArray(final int index, final int[] value) {
        this.write(int[].class, index, value);
    }
    
    @Override
    public void writeLongArray(final int index, final long[] value) {
        this.write(long[].class, index, value);
    }
    
    @Override
    public void writeFloatArray(final int index, final float[] value) {
        this.write(float[].class, index, value);
    }
    
    @Override
    public void writeDoubleArray(final int index, final double[] value) {
        this.write(double[].class, index, value);
    }
    
    @Override
    public void writeStringArray(final int index, final String[] value) {
        this.write(String[].class, index, value);
    }
    
    @Override
    public void writeAnyObject(final int index, final Object value) {
        try {
            final Field f = this.packetClass.getDeclaredFields()[index];
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            f.set(this.packet.getRawNMSPacket(), value);
        }
        catch (final Exception e) {
            throw new WrapperFieldNotFoundException("PacketEvents failed to find any field indexed " + index + " in the " + ClassUtil.getClassSimpleName(this.packetClass) + " class!");
        }
    }
    
    @Override
    public void writeEnumConstant(final int index, final Enum<?> enumConstant) {
        try {
            this.write(enumConstant.getClass(), index, enumConstant);
        }
        catch (final WrapperFieldNotFoundException ex) {
            this.write(enumConstant.getDeclaringClass(), index, enumConstant);
        }
    }
    
    public void write(final Class<?> type, final int index, final Object value) throws WrapperFieldNotFoundException {
        final Field field = this.getField(type, index);
        if (field == null) {
            throw new WrapperFieldNotFoundException(this.packetClass, type, index);
        }
        try {
            field.set(this.packet.getRawNMSPacket(), value);
        }
        catch (final IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    public Vector3i readBlockPosition(final int index) {
        final Object blockPosObj = this.readObject(index, NMSUtils.blockPosClass);
        try {
            final int x = (int)NMSUtils.getBlockPosX.invoke(blockPosObj, new Object[0]);
            final int y = (int)NMSUtils.getBlockPosY.invoke(blockPosObj, new Object[0]);
            final int z = (int)NMSUtils.getBlockPosZ.invoke(blockPosObj, new Object[0]);
            return new Vector3i(x, y, z);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Vector3i readSectionPosition(final int index) {
        final Object blockPosObj = this.readObject(index, NMSUtils.sectionPositionClass);
        try {
            final int x = (int)NMSUtils.getBlockPosX.invoke(blockPosObj, new Object[0]);
            final int y = (int)NMSUtils.getBlockPosY.invoke(blockPosObj, new Object[0]);
            final int z = (int)NMSUtils.getBlockPosZ.invoke(blockPosObj, new Object[0]);
            return new Vector3i(x, y, z);
        }
        catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void writeBlockPosition(final int index, final Vector3i blockPosition) {
        final Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPosition);
        this.write(NMSUtils.blockPosClass, index, blockPosObj);
    }
    
    public ItemStack readItemStack(final int index) {
        final Object nmsItemStack = this.readObject(index, NMSUtils.nmsItemStackClass);
        return NMSUtils.toBukkitItemStack(nmsItemStack);
    }
    
    public void writeItemStack(final int index, final ItemStack stack) {
        final Object nmsItemStack = NMSUtils.toNMSItemStack(stack);
        this.write(NMSUtils.nmsItemStackClass, index, nmsItemStack);
    }
    
    @Nullable
    public GameMode readGameMode(final int index) {
        final Enum<?> enumConst = this.readEnumConstant(index, NMSUtils.enumGameModeClass);
        final int targetIndex = enumConst.ordinal() - 1;
        if (targetIndex == -1) {
            return null;
        }
        return GameMode.values()[targetIndex];
    }
    
    public void writeGameMode(final int index, @Nullable final GameMode gameMode) {
        final int i = (gameMode != null) ? (gameMode.ordinal() + 1) : 0;
        final Enum<?> enumConst = EnumUtil.valueByIndex(NMSUtils.enumGameModeClass, i);
        this.writeEnumConstant(index, enumConst);
    }
    
    public Dimension readDimension(final int index, final int dimensionIDLegacyIndex) {
        int dimensionID;
        if (WrappedPacket.version.isOlderThan(ServerVersion.v_1_13_2)) {
            dimensionID = this.readInt(dimensionIDLegacyIndex);
        }
        else {
            final Object dimensionManagerObject = this.readObject(index, NMSUtils.dimensionManagerClass);
            final WrappedPacket dimensionManagerWrapper = new WrappedPacket(new NMSPacket(dimensionManagerObject));
            dimensionID = dimensionManagerWrapper.readInt(0) - 1;
        }
        return Dimension.getById(dimensionID);
    }
    
    public void writeDimension(final int index, final int dimensionIDLegacyIndex, final Dimension dimension) {
        if (WrappedPacket.version.isOlderThan(ServerVersion.v_1_13_2)) {
            this.writeInt(dimensionIDLegacyIndex, dimension.getId());
        }
        else {
            final Object dimensionManagerObject = this.readObject(index, NMSUtils.dimensionManagerClass);
            final WrappedPacket dimensionManagerWrapper = new WrappedPacket(new NMSPacket(dimensionManagerObject));
            dimensionManagerWrapper.writeInt(0, dimension.getId() + 1);
        }
    }
    
    public Difficulty readDifficulty(final int index) {
        final Enum<?> enumConstant = this.readEnumConstant(index, NMSUtils.enumDifficultyClass);
        return Difficulty.values()[enumConstant.ordinal()];
    }
    
    public void writeDifficulty(final int index, final Difficulty difficulty) {
        final Enum<?> enumConstant = EnumUtil.valueByIndex(NMSUtils.enumDifficultyClass, difficulty.ordinal());
        this.writeEnumConstant(index, enumConstant);
    }
    
    public String readIChatBaseComponent(final int index) {
        final Object iChatBaseComponent = this.readObject(index, NMSUtils.iChatBaseComponentClass);
        return NMSUtils.readIChatBaseComponent(iChatBaseComponent);
    }
    
    public void writeIChatBaseComponent(final int index, final String content) {
        final Object iChatBaseComponent = NMSUtils.generateIChatBaseComponent(content);
        this.write(NMSUtils.iChatBaseComponentClass, index, iChatBaseComponent);
    }
    
    public String readMinecraftKey(final int index) {
        if (WrappedPacket.isVersion_1_17 == -1) {
            WrappedPacket.isVersion_1_17 = (byte)(WrappedPacket.version.isNewerThanOrEquals(ServerVersion.v_1_17) ? 1 : 0);
        }
        final int namespaceIndex = (WrappedPacket.isVersion_1_17 == 1) ? 2 : 0;
        final int keyIndex = (WrappedPacket.isVersion_1_17 == 1) ? 3 : 1;
        final Object minecraftKey = this.readObject(index, NMSUtils.minecraftKeyClass);
        final WrappedPacket minecraftKeyWrapper = new WrappedPacket(new NMSPacket(minecraftKey));
        return minecraftKeyWrapper.readString(namespaceIndex) + ":" + minecraftKeyWrapper.readString(keyIndex);
    }
    
    public void writeMinecraftKey(final int index, final String content) {
        Object minecraftKey = null;
        try {
            minecraftKey = NMSUtils.minecraftKeyConstructor.newInstance(content);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        this.write(NMSUtils.minecraftKeyClass, index, minecraftKey);
    }
    
    public List<Object> readList(final int index) {
        return this.read(index, (Class<? extends List<Object>>)List.class);
    }
    
    public void writeList(final int index, final List<Object> list) {
        this.write(List.class, index, list);
    }
    
    public <T, K> ConditionalValue<T, K> readEither(final int index) {
        final Object either = this.readObject(index, NMSUtils.mojangEitherClass);
        final Optional<T> left = MojangEitherUtil.getLeft(either);
        if (left.isPresent()) {
            return ConditionalValue.makeLeft(left.get());
        }
        final Optional<K> right = MojangEitherUtil.getRight(either);
        try {
            return ConditionalValue.makeRight(right.orElseThrow(() -> new IllegalStateException("Conditional value has no values. Atleast one side must have a value!")));
        }
        catch (final Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public <T, K> void writeEither(final int index, final ConditionalValue<T, K> conditionalValue) {
        Object either;
        if (conditionalValue.left().isPresent()) {
            either = MojangEitherUtil.makeLeft(conditionalValue.left().get());
        }
        else {
            either = MojangEitherUtil.makeRight(conditionalValue.right().get());
        }
        this.write(NMSUtils.mojangEitherClass, index, either);
    }
    
    private Field getField(final Class<?> type, final int index) {
        final Map<Class<?>, Field[]> cached = WrappedPacket.FIELD_CACHE.computeIfAbsent(this.packetClass, k -> new ConcurrentHashMap());
        final Field[] fields = cached.computeIfAbsent(type, typeClass -> this.getFields(typeClass, this.packetClass.getDeclaredFields()));
        if (fields.length >= index + 1) {
            return fields[index];
        }
        throw new WrapperFieldNotFoundException(this.packetClass, type, index);
    }
    
    private Field[] getFields(final Class<?> type, final Field[] fields) {
        final List<Field> ret = new ArrayList<Field>();
        for (final Field field : fields) {
            if (field.getType().equals(type)) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                ret.add(field);
            }
        }
        return ret.toArray(WrappedPacket.EMPTY_FIELD_ARRAY);
    }
    
    public boolean isSupported() {
        return true;
    }
    
    static {
        LOADED_WRAPPERS = new ConcurrentHashMap<Class<? extends WrappedPacket>, Boolean>();
        FIELD_CACHE = new ConcurrentHashMap<Class<?>, Map<Class<?>, Field[]>>();
        EMPTY_FIELD_ARRAY = new Field[0];
        WrappedPacket.isVersion_1_17 = -1;
    }
    
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SupportedVersions {
        ServerVersion[] ranges() default {};
    }
}
