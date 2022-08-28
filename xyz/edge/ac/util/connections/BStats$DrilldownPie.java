package xyz.edge.ac.util.connections;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public static class DrilldownPie extends CustomChart
{
    private final Callable<Map<String, Map<String, Integer>>> callable;
    
    public DrilldownPie(final String chartId, final Callable<Map<String, Map<String, Integer>>> callable) {
        super(chartId);
        this.callable = callable;
    }
    
    public JsonObjectBuilder.JsonObject getChartData() throws Exception {
        final JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
        final Map<String, Map<String, Integer>> map = this.callable.call();
        if (map == null || map.isEmpty()) {
            return null;
        }
        boolean reallyAllSkipped = true;
        for (final Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
            final JsonObjectBuilder valueBuilder = new JsonObjectBuilder();
            boolean allSkipped = true;
            for (final Map.Entry<String, Integer> valueEntry : map.get(entryValues.getKey()).entrySet()) {
                valueBuilder.appendField(valueEntry.getKey(), valueEntry.getValue());
                allSkipped = false;
            }
            if (!allSkipped) {
                reallyAllSkipped = false;
                valuesBuilder.appendField(entryValues.getKey(), valueBuilder.build());
            }
        }
        if (reallyAllSkipped) {
            return null;
        }
        return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
    }
}
