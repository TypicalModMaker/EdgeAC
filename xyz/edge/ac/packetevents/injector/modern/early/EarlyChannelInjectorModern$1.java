package xyz.edge.ac.packetevents.injector.modern.early;

import io.netty.channel.ChannelFuture;
import java.util.List;
import xyz.edge.ac.packetevents.utils.list.ListWrapper;

class EarlyChannelInjectorModern$1 extends ListWrapper {
    @Override
    public void processAdd(final Object o) {
        if (o instanceof ChannelFuture) {
            try {
                EarlyChannelInjectorModern.access$000(EarlyChannelInjectorModern.this, (ChannelFuture)o);
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}