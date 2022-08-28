package xyz.edge.ac.packetevents.packetwrappers.play.out.chat;

import java.util.Optional;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.util.UUID;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public final class WrappedPacketOutChat extends WrappedPacket implements SendableWrapper
{
    private static Constructor<?> chatClassConstructor;
    private static Class<? extends Enum<?>> chatMessageTypeEnum;
    private static byte constructorMode;
    private String message;
    private ChatPosition chatPosition;
    private UUID uuid;
    
    public WrappedPacketOutChat(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutChat(final String message, final UUID uuid, final boolean isJson) {
        this(message, ChatPosition.CHAT, uuid, isJson);
    }
    
    public WrappedPacketOutChat(final BaseComponent component, final UUID uuid) {
        this(component, ChatPosition.CHAT, uuid);
    }
    
    public WrappedPacketOutChat(final BaseComponent component, final ChatPosition pos, final UUID uuid) {
        this(ComponentSerializer.toString(component), pos, uuid, true);
    }
    
    public WrappedPacketOutChat(final String message, final ChatPosition chatPosition, final UUID uuid, final boolean isJson) {
        this.uuid = uuid;
        this.message = (isJson ? message : NMSUtils.fromStringToJSON(message));
        this.chatPosition = chatPosition;
    }
    
    @Override
    protected void load() {
        final Class<?> packetClass = PacketTypeClasses.Play.Server.CHAT;
        WrappedPacketOutChat.chatMessageTypeEnum = NMSUtils.getNMSEnumClassWithoutException("ChatMessageType");
        if (WrappedPacketOutChat.chatMessageTypeEnum == null) {
            WrappedPacketOutChat.chatMessageTypeEnum = NMSUtils.getNMEnumClassWithoutException("network.chat.ChatMessageType");
        }
        if (WrappedPacketOutChat.chatMessageTypeEnum != null) {
            try {
                WrappedPacketOutChat.chatClassConstructor = packetClass.getConstructor(NMSUtils.iChatBaseComponentClass, WrappedPacketOutChat.chatMessageTypeEnum);
                WrappedPacketOutChat.constructorMode = 2;
            }
            catch (final NoSuchMethodException e) {
                try {
                    WrappedPacketOutChat.chatClassConstructor = packetClass.getConstructor(NMSUtils.iChatBaseComponentClass, WrappedPacketOutChat.chatMessageTypeEnum, UUID.class);
                    WrappedPacketOutChat.constructorMode = 3;
                }
                catch (final NoSuchMethodException e2) {
                    e2.printStackTrace();
                }
            }
        }
        else {
            try {
                WrappedPacketOutChat.chatClassConstructor = packetClass.getConstructor(NMSUtils.iChatBaseComponentClass, Byte.TYPE);
                WrappedPacketOutChat.constructorMode = 0;
            }
            catch (final NoSuchMethodException e) {
                try {
                    WrappedPacketOutChat.chatClassConstructor = packetClass.getConstructor(NMSUtils.iChatBaseComponentClass, Integer.TYPE);
                    WrappedPacketOutChat.constructorMode = 1;
                }
                catch (final NoSuchMethodException e2) {
                    try {
                        WrappedPacketOutChat.chatClassConstructor = packetClass.getConstructor(NMSUtils.iChatBaseComponentClass);
                        WrappedPacketOutChat.constructorMode = -1;
                    }
                    catch (final NoSuchMethodException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        if (WrappedPacketOutChat.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            throw new IllegalStateException("You are trying to send the WrappedPacketOutChat packet on 1.19 or above. Please update to packetevents 2.0, this is not supported.");
        }
        final byte chatPos = (byte)this.getChatPosition().ordinal();
        Enum<?> chatMessageTypeInstance = null;
        if (WrappedPacketOutChat.chatMessageTypeEnum != null) {
            chatMessageTypeInstance = EnumUtil.valueByIndex(WrappedPacketOutChat.chatMessageTypeEnum, chatPos);
        }
        switch (WrappedPacketOutChat.constructorMode) {
            case -1: {
                return WrappedPacketOutChat.chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(this.getMessage()));
            }
            case 0: {
                return WrappedPacketOutChat.chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(this.getMessage()), chatPos);
            }
            case 1: {
                return WrappedPacketOutChat.chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(this.getMessage()), chatPos);
            }
            case 2: {
                return WrappedPacketOutChat.chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(this.getMessage()), chatMessageTypeInstance);
            }
            case 3: {
                return WrappedPacketOutChat.chatClassConstructor.newInstance(NMSUtils.generateIChatBaseComponent(this.getMessage()), chatMessageTypeInstance, this.uuid);
            }
            default: {
                return null;
            }
        }
    }
    
    public String getMessage() {
        if (this.packet != null) {
            return this.readIChatBaseComponent(0);
        }
        return this.message;
    }
    
    public void setMessage(final String message) {
        if (this.packet != null) {
            this.writeIChatBaseComponent(0, message);
        }
        else {
            this.message = message;
        }
    }
    
    public Optional<String> getUnsignedMessage() {
        if (WrappedPacketOutChat.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            final Optional<?> opt = this.readObject(0, (Class<? extends Optional<?>>)Optional.class);
            if (opt.isPresent()) {
                final Object iChatBaseComponent = opt.get();
                return Optional.of(NMSUtils.readIChatBaseComponent(iChatBaseComponent));
            }
        }
        return Optional.empty();
    }
    
    public void setUnsignedMessage(final String unsignedMessage) {
        if (WrappedPacketOutChat.version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            final Object iChatBaseComponent = NMSUtils.generateIChatBaseComponent(unsignedMessage);
            this.write(Optional.class, 0, Optional.of(iChatBaseComponent));
        }
    }
    
    public ChatPosition getChatPosition() {
        if (this.packet != null) {
            byte chatPositionValue = 0;
            switch (WrappedPacketOutChat.constructorMode) {
                case -1: {
                    chatPositionValue = (byte)ChatPosition.CHAT.ordinal();
                    break;
                }
                case 0: {
                    chatPositionValue = this.readByte(0);
                    break;
                }
                case 1: {
                    chatPositionValue = (byte)this.readInt(0);
                    break;
                }
                case 2:
                case 3: {
                    final Enum<?> chatTypeEnumInstance = this.readEnumConstant(0, WrappedPacketOutChat.chatMessageTypeEnum);
                    return ChatPosition.values()[chatTypeEnumInstance.ordinal()];
                }
                default: {
                    chatPositionValue = 0;
                    break;
                }
            }
            return ChatPosition.values()[chatPositionValue];
        }
        return this.chatPosition;
    }
    
    public void setChatPosition(final ChatPosition chatPosition) {
        if (this.packet != null) {
            switch (WrappedPacketOutChat.constructorMode) {
                case 0: {
                    this.writeByte(0, (byte)chatPosition.ordinal());
                    break;
                }
                case 1: {
                    this.writeInt(0, chatPosition.ordinal());
                    break;
                }
                case 2:
                case 3: {
                    final Enum<?> chatTypeEnumInstance = EnumUtil.valueByIndex(WrappedPacketOutChat.chatMessageTypeEnum, chatPosition.ordinal());
                    this.writeEnumConstant(0, chatTypeEnumInstance);
                    break;
                }
            }
        }
        else {
            this.chatPosition = chatPosition;
        }
    }
    
    public enum ChatPosition
    {
        CHAT, 
        SYSTEM_MESSAGE, 
        GAME_INFO, 
        SAY_COMMAND, 
        MSG_COMMAND, 
        TEAM_MSG_COMMAND, 
        EMOTE_COMMAND, 
        TELLRAW_COMMAND;
    }
}
