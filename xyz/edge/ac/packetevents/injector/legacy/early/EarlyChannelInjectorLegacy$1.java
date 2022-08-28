package xyz.edge.ac.packetevents.injector.legacy.early;

import net.minecraft.util.io.netty.channel.ChannelFuture;
import java.util.List;
import xyz.edge.ac.packetevents.utils.list.ListWrapper;

class EarlyChannelInjectorLegacy$1 extends ListWrapper {
    @Override
    public void processAdd(final Object o) {
        if (o instanceof ChannelFuture) {
            try {
                EarlyChannelInjectorLegacy.access$000(EarlyChannelInjectorLegacy.this, (ChannelFuture)o);
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}