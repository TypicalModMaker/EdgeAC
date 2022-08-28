package xyz.edge.ac.packetevents.packetwrappers.play.out.playerinfo;

import org.jetbrains.annotations.Nullable;
import xyz.edge.ac.packetevents.PacketEvents;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import xyz.edge.ac.packetevents.utils.gameprofile.WrappedGameProfile;
import xyz.edge.ac.packetevents.utils.player.GameMode;
import xyz.edge.ac.packetevents.utils.gameprofile.GameProfileUtil;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutPlayerInfo extends WrappedPacket implements SendableWrapper
{
    private static boolean v_1_7_10;
    private static boolean v_1_17;
    private static Class<? extends Enum<?>> enumPlayerInfoActionClass;
    private static Constructor<?> packetConstructor;
    private static Constructor<?> playerInfoDataConstructor;
    private static byte constructorMode;
    private PlayerInfoAction action;
    private PlayerInfo[] playerInfoArray;
    
    public WrappedPacketOutPlayerInfo(final NMSPacket packet) {
        super(packet);
        this.playerInfoArray = new PlayerInfo[0];
    }
    
    public WrappedPacketOutPlayerInfo(final PlayerInfoAction action, final PlayerInfo... playerInfoArray) {
        this.playerInfoArray = new PlayerInfo[0];
        this.action = action;
        this.playerInfoArray = playerInfoArray;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutPlayerInfo.v_1_7_10 = WrappedPacketOutPlayerInfo.version.isOlderThan(ServerVersion.v_1_8);
        WrappedPacketOutPlayerInfo.v_1_17 = WrappedPacketOutPlayerInfo.version.isNewerThanOrEquals(ServerVersion.v_1_17);
        WrappedPacketOutPlayerInfo.enumPlayerInfoActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Server.PLAYER_INFO, "EnumPlayerInfoAction");
        try {
            if (WrappedPacketOutPlayerInfo.v_1_17) {
                WrappedPacketOutPlayerInfo.packetConstructor = PacketTypeClasses.Play.Server.PLAYER_INFO.getConstructor(NMSUtils.packetDataSerializerClass);
            }
            else {
                WrappedPacketOutPlayerInfo.packetConstructor = PacketTypeClasses.Play.Server.PLAYER_INFO.getConstructor((Class<?>[])new Class[0]);
            }
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
        final Class<?> playerInfoDataClass = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.PLAYER_INFO, "PlayerInfoData");
        if (playerInfoDataClass != null) {
            try {
                WrappedPacketOutPlayerInfo.playerInfoDataConstructor = playerInfoDataClass.getConstructor(NMSUtils.gameProfileClass, Integer.TYPE, NMSUtils.enumGameModeClass, NMSUtils.iChatBaseComponentClass);
            }
            catch (final NoSuchMethodException e2) {
                try {
                    WrappedPacketOutPlayerInfo.playerInfoDataConstructor = playerInfoDataClass.getConstructor(PacketTypeClasses.Play.Server.PLAYER_INFO, NMSUtils.gameProfileClass, Integer.TYPE, NMSUtils.enumGameModeClass, NMSUtils.iChatBaseComponentClass);
                    WrappedPacketOutPlayerInfo.constructorMode = 1;
                }
                catch (final NoSuchMethodException e3) {
                    e2.printStackTrace();
                    e3.printStackTrace();
                }
            }
        }
    }
    
    public PlayerInfoAction getAction() {
        if (this.packet != null) {
            int index;
            if (WrappedPacketOutPlayerInfo.v_1_7_10) {
                index = this.readInt(0);
            }
            else {
                final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketOutPlayerInfo.enumPlayerInfoActionClass);
                index = enumConst.ordinal();
            }
            return PlayerInfoAction.values()[index];
        }
        return this.action;
    }
    
    public void setAction(final PlayerInfoAction action) {
        if (this.packet != null) {
            final int index = action.ordinal();
            if (WrappedPacketOutPlayerInfo.v_1_7_10) {
                this.writeInt(0, index);
            }
            else {
                final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketOutPlayerInfo.enumPlayerInfoActionClass, action.ordinal());
                this.writeEnumConstant(0, enumConst);
            }
        }
        else {
            this.action = action;
        }
    }
    
    public PlayerInfo[] getPlayerInfo() {
        if (this.packet != null) {
            final PlayerInfo[] playerInfoArray = { null };
            if (WrappedPacketOutPlayerInfo.v_1_7_10) {
                final String username = this.readString(0);
                final Object mojangGameProfile = this.readObject(0, NMSUtils.gameProfileClass);
                final WrappedGameProfile gameProfile = GameProfileUtil.getWrappedGameProfile(mojangGameProfile);
                final GameMode gameMode = GameMode.values()[this.readInt(1)];
                final int ping = this.readInt(2);
                playerInfoArray[0] = new PlayerInfo(username, gameProfile, gameMode, ping);
            }
            else {
                final List<Object> nmsPlayerInfoDataList = this.readList(0);
                for (int i = 0; i < nmsPlayerInfoDataList.size(); ++i) {
                    final Object nmsPlayerInfoData = nmsPlayerInfoDataList.get(i);
                    final WrappedPacket nmsPlayerInfoDataWrapper = new WrappedPacket(new NMSPacket(nmsPlayerInfoData));
                    final String username2 = nmsPlayerInfoDataWrapper.readIChatBaseComponent(0);
                    final Object mojangGameProfile2 = nmsPlayerInfoDataWrapper.readObject(0, NMSUtils.gameProfileClass);
                    final WrappedGameProfile gameProfile2 = GameProfileUtil.getWrappedGameProfile(mojangGameProfile2);
                    final GameMode gameMode2 = nmsPlayerInfoDataWrapper.readGameMode(0);
                    final int ping2 = nmsPlayerInfoDataWrapper.readInt(0);
                    playerInfoArray[i] = new PlayerInfo(username2, gameProfile2, gameMode2, ping2);
                }
            }
            return playerInfoArray;
        }
        return this.playerInfoArray;
    }
    
    public void setPlayerInfo(final PlayerInfo... playerInfoArray) throws UnsupportedOperationException {
        if (WrappedPacketOutPlayerInfo.v_1_7_10 && playerInfoArray.length > 1) {
            throw new UnsupportedOperationException("The player info list size cannot be greater than 1 on 1.7.10 servers!");
        }
        if (this.packet != null) {
            if (WrappedPacketOutPlayerInfo.v_1_7_10) {
                final PlayerInfo playerInfo = playerInfoArray[0];
                this.writeString(0, playerInfo.username);
                final Object mojangGameProfile = GameProfileUtil.getGameProfile(playerInfo.gameProfile.getId(), playerInfo.gameProfile.getName());
                this.writeObject(0, mojangGameProfile);
                this.writeInt(1, playerInfo.gameMode.ordinal());
                this.writeInt(2, playerInfo.ping);
            }
            else {
                final List<Object> nmsPlayerInfoList = new ArrayList<Object>();
                for (final PlayerInfo playerInfo2 : playerInfoArray) {
                    final Object usernameIChatBaseComponent = NMSUtils.generateIChatBaseComponent(NMSUtils.fromStringToJSON(playerInfo2.username));
                    final Object mojangGameProfile2 = GameProfileUtil.getGameProfile(playerInfo2.gameProfile.getId(), playerInfo2.gameProfile.getName());
                    final Enum<?> nmsGameModeEnumConstant = EnumUtil.valueByIndex(NMSUtils.enumGameModeClass, playerInfo2.gameMode.ordinal());
                    final int ping = playerInfo2.ping;
                    try {
                        if (WrappedPacketOutPlayerInfo.constructorMode == 0) {
                            nmsPlayerInfoList.add(WrappedPacketOutPlayerInfo.playerInfoDataConstructor.newInstance(mojangGameProfile2, ping, nmsGameModeEnumConstant, usernameIChatBaseComponent));
                        }
                        else if (WrappedPacketOutPlayerInfo.constructorMode == 1) {
                            nmsPlayerInfoList.add(WrappedPacketOutPlayerInfo.playerInfoDataConstructor.newInstance(null, mojangGameProfile2, ping, nmsGameModeEnumConstant, usernameIChatBaseComponent));
                        }
                    }
                    catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                this.writeList(0, nmsPlayerInfoList);
            }
        }
        else {
            this.playerInfoArray = playerInfoArray;
        }
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        Object packetInstance;
        if (WrappedPacketOutPlayerInfo.v_1_17) {
            final Object byteBuf = PacketEvents.get().getByteBufUtil().newByteBuf(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
            final Object packetDataSerializer = NMSUtils.generatePacketDataSerializer(byteBuf);
            packetInstance = WrappedPacketOutPlayerInfo.packetConstructor.newInstance(packetDataSerializer);
        }
        else {
            packetInstance = WrappedPacketOutPlayerInfo.packetConstructor.newInstance(new Object[0]);
        }
        final WrappedPacketOutPlayerInfo playerInfoWrapper = new WrappedPacketOutPlayerInfo(new NMSPacket(packetInstance));
        final PlayerInfo[] playerInfos = this.getPlayerInfo();
        if (playerInfos.length != 0) {
            playerInfoWrapper.setPlayerInfo(playerInfos);
        }
        playerInfoWrapper.setAction(this.getAction());
        return packetInstance;
    }
    
    static {
        WrappedPacketOutPlayerInfo.constructorMode = 0;
    }
    
    public enum PlayerInfoAction
    {
        ADD_PLAYER, 
        UPDATE_GAME_MODE, 
        UPDATE_LATENCY, 
        UPDATE_DISPLAY_NAME, 
        REMOVE_PLAYER;
    }
    
    public static class PlayerInfo
    {
        private String username;
        private WrappedGameProfile gameProfile;
        private GameMode gameMode;
        private int ping;
        
        public PlayerInfo(@Nullable final String username, final WrappedGameProfile gameProfile, final GameMode gameMode, final int ping) {
            this.username = username;
            this.gameProfile = gameProfile;
            this.gameMode = gameMode;
            this.ping = ping;
        }
        
        public PlayerInfo(@Nullable final String username, final WrappedGameProfile gameProfile, final org.bukkit.GameMode gameMode, final int ping) {
            this.username = username;
            this.gameProfile = gameProfile;
            this.gameMode = GameMode.valueOf(gameMode.name());
            this.ping = ping;
        }
        
        public WrappedGameProfile getGameProfile() {
            return this.gameProfile;
        }
        
        public void setGameProfile(final WrappedGameProfile gameProfile) {
            this.gameProfile = gameProfile;
        }
        
        public GameMode getGameMode() {
            return this.gameMode;
        }
        
        public void setGameMode(final GameMode gameMode) {
            this.gameMode = gameMode;
        }
        
        public int getPing() {
            return this.ping;
        }
        
        public void setPing(final int ping) {
            this.ping = ping;
        }
        
        @Nullable
        public String getUsername() {
            return this.username;
        }
        
        public void setUsername(@Nullable final String username) {
            this.username = username;
        }
    }
}
