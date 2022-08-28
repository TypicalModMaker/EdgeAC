package xyz.edge.ac.packetevents.packetwrappers.play.out.title;

import xyz.edge.ac.packetevents.utils.server.ServerVersion;
import xyz.edge.ac.packetevents.utils.enums.EnumUtil;
import xyz.edge.ac.packetevents.utils.nms.NMSUtils;
import xyz.edge.ac.packetevents.utils.reflection.SubclassUtil;
import xyz.edge.ac.packetevents.packettype.PacketTypeClasses;
import xyz.edge.ac.packetevents.packetwrappers.NMSPacket;
import java.lang.reflect.Constructor;
import xyz.edge.ac.packetevents.packetwrappers.api.SendableWrapper;
import xyz.edge.ac.packetevents.packetwrappers.WrappedPacket;

public class WrappedPacketOutTitle extends WrappedPacket implements SendableWrapper
{
    private static Class<? extends Enum<?>> enumTitleActionClass;
    private static Constructor<?> packetConstructor;
    private TitleAction action;
    private String text;
    private int fadeInTicks;
    private int stayTicks;
    private int fadeOutTicks;
    
    public WrappedPacketOutTitle(final NMSPacket packet) {
        super(packet);
    }
    
    public WrappedPacketOutTitle(final TitleAction action, final String text, final int fadeInTicks, final int stayTicks, final int fadeOutTicks) {
        this.action = action;
        this.text = text;
        this.fadeInTicks = fadeInTicks;
        this.stayTicks = stayTicks;
        this.fadeOutTicks = fadeOutTicks;
    }
    
    @Override
    protected void load() {
        WrappedPacketOutTitle.enumTitleActionClass = SubclassUtil.getEnumSubClass(PacketTypeClasses.Play.Server.TITLE, 0);
        try {
            WrappedPacketOutTitle.packetConstructor = PacketTypeClasses.Play.Server.TITLE.getConstructor(WrappedPacketOutTitle.enumTitleActionClass, NMSUtils.iChatBaseComponentClass, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        }
        catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    public TitleAction getAction() {
        if (this.packet != null) {
            final Enum<?> enumConst = this.readEnumConstant(0, WrappedPacketOutTitle.enumTitleActionClass);
            return TitleAction.values()[enumConst.ordinal()];
        }
        return this.action;
    }
    
    public void setAction(final TitleAction action) {
        if (this.packet != null) {
            final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketOutTitle.enumTitleActionClass, action.ordinal());
            this.writeEnumConstant(0, enumConst);
        }
        else {
            this.action = action;
        }
    }
    
    public String getText() {
        if (this.packet != null) {
            return this.readIChatBaseComponent(0);
        }
        return this.text;
    }
    
    public void setText(final String text) {
        if (this.packet != null) {
            this.writeIChatBaseComponent(0, text);
        }
        else {
            this.text = text;
        }
    }
    
    public int getFadeInTicks() {
        if (this.packet != null) {
            return this.readInt(0);
        }
        return this.fadeInTicks;
    }
    
    public void setFadeInTicks(final int fadeInTicks) {
        if (this.packet != null) {
            this.writeInt(0, fadeInTicks);
        }
        this.fadeInTicks = fadeInTicks;
    }
    
    public int getStayTicks() {
        if (this.packet != null) {
            return this.readInt(1);
        }
        return this.stayTicks;
    }
    
    public void setStayTicks(final int stayTicks) {
        if (this.packet != null) {
            this.writeInt(1, stayTicks);
        }
        else {
            this.stayTicks = stayTicks;
        }
    }
    
    public int getFadeOutTicks() {
        if (this.packet != null) {
            return this.readInt(2);
        }
        return this.fadeOutTicks;
    }
    
    public void setFadeOutTicks(final int fadeOutTicks) {
        if (this.packet != null) {
            this.writeInt(2, fadeOutTicks);
        }
        else {
            this.fadeOutTicks = fadeOutTicks;
        }
    }
    
    @Override
    public boolean isSupported() {
        return WrappedPacketOutTitle.version.isNewerThan(ServerVersion.v_1_7_10) && WrappedPacketOutTitle.version.isOlderThan(ServerVersion.v_1_17);
    }
    
    @Override
    public Object asNMSPacket() throws Exception {
        final Enum<?> enumConst = EnumUtil.valueByIndex(WrappedPacketOutTitle.enumTitleActionClass, this.getAction().ordinal());
        return WrappedPacketOutTitle.packetConstructor.newInstance(enumConst, NMSUtils.generateIChatBaseComponent(this.getText()), this.getFadeInTicks(), this.getStayTicks(), this.getFadeOutTicks());
    }
    
    public enum TitleAction
    {
        TITLE, 
        SUBTITLE, 
        TIMES, 
        CLEAR, 
        RESET;
    }
}
