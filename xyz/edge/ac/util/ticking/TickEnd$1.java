package xyz.edge.ac.util.ticking;

import java.util.List;
import xyz.edge.ac.wrappers.HookedListWrapper;

class TickEnd$1 extends HookedListWrapper<Object> {
    @Override
    public void onIterator() {
        TickEnd.this.hasTicked = true;
        TickEnd.access$000();
    }
}