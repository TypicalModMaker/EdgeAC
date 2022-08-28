package xyz.edge.ac.packetevents.packetwrappers.play.out.entityequipment;

import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.PacketEvents;
import java.util.Iterator;
import xyz.edge.ac.packetevents.utils.pair.MojangPairUtils;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Entity;
import java.util.ArrayList;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import org.bukkit.inventory.ItemStack;
import xyz.edge.ac.packetevents.utils.pair.Pair;
import java.util.List;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

public class WrappedPacketOutEntityEquipment extends WrappedPacketEntityAbstraction implements SendableWrapper
{
    private static boolean v_1_17;
    private static Class<? extends Enum<?>> enumItemSlotClass;
    private static Constructor<?> packetConstructor;
    private List<Pair<EquipmentSlot, ItemStack>> equipment;
    private EquipmentSlot legacySlot;
    private ItemStack legacyItemStack;
    
    public WrappedPacketOutEntityEquipment(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutEntityEquipment(final int entityID, final EquipmentSlot slot, final ItemStack itemStack) {
        this.setEntityId(entityID);
        final Pair<EquipmentSlot, ItemStack> pair = new Pair<EquipmentSlot, ItemStack>(slot, itemStack);
        (this.equipment = new ArrayList<Pair<EquipmentSlot, ItemStack>>()).add(pair);
        this.legacySlot = slot;
        this.legacyItemStack = itemStack;
    }
    
    public WrappedPacketOutEntityEquipment(final Entity entity, final EquipmentSlot slot, final ItemStack itemStack) {
        this.setEntity(entity);
        final Pair<EquipmentSlot, ItemStack> pair = new Pair<EquipmentSlot, ItemStack>(slot, itemStack);
        (this.equipment = new ArrayList<Pair<EquipmentSlot, ItemStack>>()).add(pair);
        this.legacySlot = slot;
        this.legacyItemStack = itemStack;
    }
    
    public WrappedPacketOutEntityEquipment(final int entityID, final List<Pair<EquipmentSlot, ItemStack>> equipment) {
        this.setEntityId(entityID);
        this.equipment = equipment;
        this.legacySlot = (EquipmentSlot)equipment.get(0).getFirst();
        this.legacyItemStack = (ItemStack)equipment.get(0).getSecond();
    }
    
    public WrappedPacketOutEntityEquipment(final Entity entity, final List<Pair<EquipmentSlot, ItemStack>> equipment) {
        this.setEntity(entity);
        this.equipment = equipment;
        this.legacySlot = (EquipmentSlot)equipment.get(0).getFirst();
        this.legacyItemStack = (ItemStack)equipment.get(0).getSecond();
    }
    
    @Override
    protected void load() {
        WrappedPacketOutEntityEquipment.v_1_17 = WrappedPacketOutEntityEquipment.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (WrappedPacketOutEntityEquipment.v_1_17) {
                WrappedPacketOutEntityEquipment.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EQUIPMENT.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutEntityEquipment.packetConstructor = PacketTypeClasses.Play.Server.ENTITY_EQUIPMENT.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
        WrappedPacketOutEntityEquipment.enumItemSlotClass = NMSUtils.getNMSEnumClassWithoutException("EnumItemSlot");
        if (WrappedPacketOutEntityEquipment.enumItemSlotClass == null) {
            WrappedPacketOutEntityEquipment.enumItemSlotClass = NMSUtils.getNMEnumClassWithoutException("world.entity.EnumItemSlot");
        }
    }
    
    private EquipmentSlot getSingleSlot() {
        if (this.packet != null) {
            byte id;
            if (WrappedPacketOutEntityEquipment.version.isOlderThan(ServerVersion.v_1_9)) {
                id = (byte)this.readInt(1);
            }
            else {
                final Enum<?> nmsEnumItemSlot = this.readEnumConstant(0, WrappedPacketOutEntityEquipment.enumItemSlotClass);
                id = (byte)nmsEnumItemSlot.ordinal();
            }
            return EquipmentSlot.getById(id);
        }
        return this.legacySlot;
    }
    
    private void setSingleSlot(final EquipmentSlot slot) {
        if (this.packet != null) {
            if (WrappedPacketOutEntityEquipment.version.isOlderThan(ServerVersion.v_1_9)) {
                this.writeInt(1, slot.getId());
            }
            else {
                final Enum<?> nmsEnumConstant = EnumUtil.valueByIndex(WrappedPacketOutEntityEquipment.enumItemSlotClass, slot.getId());
                this.writeEnumConstant(0, nmsEnumConstant);
            }
        }
        else {
            this.legacySlot = slot;
        }
    }
    
    private ItemStack getSingleItemStack() {
        if (this.packet != null) {
            return this.readItemStack(0);
        }
        return this.legacyItemStack;
    }
    
    private void setSingleItemStack(final ItemStack itemStack) {
        if (this.packet != null) {
            this.writeItemStack(0, itemStack);
        }
        else {
            this.legacyItemStack = itemStack;
        }
    }
    
    private List<Pair<EquipmentSlot, ItemStack>> getListPair() {
        final List<Object> listMojangPairObject = this.readList(0);
        final List<Pair<EquipmentSlot, ItemStack>> pairList = new ArrayList<Pair<EquipmentSlot, ItemStack>>();
        for (final Object mojangPair : listMojangPairObject) {
            final Pair<Object, Object> abstractedPair = MojangPairUtils.extractPair(mojangPair);
            final Enum<?> nmsItemSlot = abstractedPair.getFirst();
            final Object nmsItemStack = abstractedPair.getSecond();
            final Pair<EquipmentSlot, ItemStack> pair = new Pair<EquipmentSlot, ItemStack>(EquipmentSlot.getById((byte)nmsItemSlot.ordinal()), NMSUtils.toBukkitItemStack(nmsItemStack));
            pairList.add(pair);
        }
        return pairList;
    }
    
    private void setListPair(final List<Pair<EquipmentSlot, ItemStack>> pairList) {
        final List<Object> mojangPairList = new ArrayList<Object>(pairList.size());
        for (final Pair<EquipmentSlot, ItemStack> pair : pairList) {
            final EquipmentSlot slot = pair.getFirst();
            final ItemStack itemStack = pair.getSecond();
            final Enum<?> nmsItemSlotEnumConstant = EnumUtil.valueByIndex(WrappedPacketOutEntityEquipment.enumItemSlotClass, slot.getId());
            final Object nmsItemStack = NMSUtils.toNMSItemStack(itemStack);
            final Object mojangPair = MojangPairUtils.getMojangPair(nmsItemSlotEnumConstant, nmsItemStack);
            mojangPairList.add(mojangPair);
        }
        this.writeList(0, mojangPairList);
    }
    
    public List<Pair<EquipmentSlot, ItemStack>> getEquipment() {
        if (this.packet == null) {
            return this.equipment;
        }
        if (WrappedPacketOutEntityEquipment.version.isOlderThan(ServerVersion.v_1_16)) {
            final List<Pair<EquipmentSlot, ItemStack>> pair = new ArrayList<Pair<EquipmentSlot, ItemStack>>(1);
            pair.add(new Pair<EquipmentSlot, ItemStack>(this.getSingleSlot(), this.getSingleItemStack()));
            return pair;
        }
        return this.getListPair();
    }
    
    public void setEquipment(final List<Pair<EquipmentSlot, ItemStack>> equipment) throws UnsupportedOperationException {
        final boolean olderThan_v_1_16 = WrappedPacketOutEntityEquipment.version.isOlderThan(ServerVersion.v_1_16);
        if (olderThan_v_1_16 && equipment.size() > 1) {
            throw new UnsupportedOperationException("The equipment pair list size cannot be greater than one on server versions older than 1.16!");
        }
        if (this.packet != null) {
            if (olderThan_v_1_16) {
                final EquipmentSlot equipmentSlot = (EquipmentSlot)equipment.get(0).getFirst();
                final ItemStack itemStack = (ItemStack)equipment.get(0).getSecond();
                this.setSingleSlot(equipmentSlot);
                this.setSingleItemStack(itemStack);
            }
            else {
                this.setListPair(equipment);
            }
        }
        else {
            this.equipment = equipment;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutEntityEquipment.v_1_17) {
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 0, 0, 0, 0 }));
            packetInstance = WrappedPacketOutEntityEquipment.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutEntityEquipment.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutEntityEquipment wrappedPacketOutEntityEquipment = new WrappedPacketOutEntityEquipment(new NMSPacket(packetInstance));
        wrappedPacketOutEntityEquipment.setEntityId(this.getEntityId());
        wrappedPacketOutEntityEquipment.setEquipment(this.getEquipment());
        return packetInstance;
    }
    
    public enum EquipmentSlot
    {
        MAINHAND, 
        OFFHAND, 
        BOOTS, 
        LEGGINGS, 
        CHESTPLATE, 
        HELMET;
        
        public byte id;
        
        @Nullable
        public static EquipmentSlot getById(final byte id) {
            for (final EquipmentSlot slot : values()) {
                if (slot.id == id) {
                    return slot;
                }
            }
            return null;
        }
        
        public byte getId() {
            return this.id;
        }
    }
}
