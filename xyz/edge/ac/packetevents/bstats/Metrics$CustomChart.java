package xyz.edge.ac.packetevents.bstats;

import java.util.function.BiConsumer;

public abstract static class CustomChart
{
    private final String chartId;
    
    protected CustomChart(final String chartId) {
        if (chartId == null) {
            throw new IllegalArgumentException("chartId must not be null");
        }
        this.chartId = chartId;
    }
    
    public JsonObjectBuilder.JsonObject getRequestJsonObject(final BiConsumer<String, Throwable> errorLogger, final boolean logErrors) {
        final JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.appendField("chartId", this.chartId);
        try {
            final JsonObjectBuilder.JsonObject data = this.getChartData();
            if (data == null) {
                return null;
            }
            builder.appendField("data", data);
        }
        catch (final Throwable t) {
            if (logErrors) {
                errorLogger.accept("Failed to get data for custom chart with id " + this.chartId, t);
            }
            return null;
        }
        return builder.build();
    }
    
    protected abstract JsonObjectBuilder.JsonObject getChartData() throws Exception;
}
