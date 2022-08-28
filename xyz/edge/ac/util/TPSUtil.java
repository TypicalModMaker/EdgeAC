package xyz.edge.ac.util;

import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.packetevents.PacketEvents;
import java.text.DecimalFormat;

public final class TPSUtil
{
    public static String getFormattedTPS() {
        return new DecimalFormat("##.##").format(PacketEvents.get().getServerUtils().getTPS());
    }
    
    public static String getTpsBar() {
        final double percent = PacketEvents.get().getServerUtils().getTPS() / 20.0 * 20.0;
        final StringBuilder result = new StringBuilder(ColorUtil.translate("&7["));
        for (int i = 0; i < 20; ++i) {
            if (i < percent) {
                result.append(ColorUtil.translate("&b\u239f"));
            }
            else {
                result.append(ColorUtil.translate("&7\u239f"));
            }
        }
        result.append(ColorUtil.translate("&7]"));
        return result.toString();
    }
    
    private TPSUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
