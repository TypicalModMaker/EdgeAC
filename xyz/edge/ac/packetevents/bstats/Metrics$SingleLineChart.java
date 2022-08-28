package xyz.edge.ac.packetevents.bstats;

import java.util.concurrent.Callable;

public static class SingleLineChart extends CustomChart
{
    private final Callable<Integer> callable;
    
    public SingleLineChart(final String chartId, final Callable<Integer> callable) {
        super(chartId);
        this.callable = callable;
    }
    
    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        final int value = this.callable.call();
        if (value == 0) {
            return null;
        }
        return new JsonObjectBuilder().appendField("value", value).build();
    }
}
