package xyz.edge.ac.util.connections;

import java.util.concurrent.Callable;

public static class SimplePie extends CustomChart
{
    private final Callable<String> callable;
    
    public SimplePie(final String chartId, final Callable<String> callable) {
        super(chartId);
        this.callable = callable;
    }
    
    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        final String value = this.callable.call();
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new JsonObjectBuilder().appendField("value", value).build();
    }
}
