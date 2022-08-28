package xyz.edge.ac.packetevents.bstats;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public static class AdvancedBarChart extends CustomChart
{
    private final Callable<Map<String, int[]>> callable;
    
    public AdvancedBarChart(final String chartId, final Callable<Map<String, int[]>> callable) {
        super(chartId);
        this.callable = callable;
    }
    
    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        final JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
        final Map<String, int[]> map = this.callable.call();
        if (map == null || map.isEmpty()) {
            return null;
        }
        boolean allSkipped = true;
        for (final Map.Entry<String, int[]> entry : map.entrySet()) {
            if (entry.getValue().length == 0) {
                continue;
            }
            allSkipped = false;
            valuesBuilder.appendField(entry.getKey(), entry.getValue());
        }
        if (allSkipped) {
            return null;
        }
        return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
    }
}
