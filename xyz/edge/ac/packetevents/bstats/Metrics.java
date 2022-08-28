package xyz.edge.ac.packetevents.bstats;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.concurrent.ScheduledExecutorService;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

public class Metrics
{
    private final Plugin plugin;
    private final MetricsBase metricsBase;
    
    public Metrics(final JavaPlugin plugin, final int serviceId) {
        this.plugin = plugin;
        final File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        final File configFile = new File(bStatsFolder, "config.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", true);
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", false);
            config.addDefault("logSentData", false);
            config.addDefault("logResponseStatusText", false);
            config.options().header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.").copyDefaults(true);
            try {
                config.save(configFile);
            }
            catch (final IOException ex) {}
        }
        final boolean enabled = config.getBoolean("enabled", true);
        final String serverUUID = config.getString("serverUuid");
        final boolean logErrors = config.getBoolean("logFailedRequests", false);
        final boolean logSentData = config.getBoolean("logSentData", false);
        final boolean logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        this.metricsBase = new MetricsBase("bukkit", serverUUID, serviceId, enabled, this::appendPlatformData, this::appendServiceData, submitDataTask -> Bukkit.getScheduler().runTask(plugin, submitDataTask), plugin::isEnabled, (message, error) -> this.plugin.getLogger().log(Level.WARNING, message, error), message -> this.plugin.getLogger().log(Level.INFO, message), logErrors, logSentData, logResponseStatusText);
    }
    
    public void addCustomChart(final CustomChart chart) {
        this.metricsBase.addCustomChart(chart);
    }
    
    private void appendPlatformData(final JsonObjectBuilder builder) {
        builder.appendField("playerAmount", this.getPlayerAmount());
        builder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
        builder.appendField("bukkitVersion", Bukkit.getVersion());
        builder.appendField("bukkitName", Bukkit.getName());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }
    
    private void appendServiceData(final JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", this.plugin.getDescription().getVersion());
    }
    
    private int getPlayerAmount() {
        try {
            final Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers", (Class<?>[])new Class[0]);
            return onlinePlayersMethod.getReturnType().equals(Collection.class) ? ((Collection)onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).size() : ((Player[])onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).length;
        }
        catch (final Exception e) {
            return Bukkit.getOnlinePlayers().size();
        }
    }
    
    public static class MetricsBase
    {
        public static final String METRICS_VERSION = "2.2.1";
        private static final ScheduledExecutorService scheduler;
        private static final String REPORT_URL = "https://bStats.org/api/v2/data/%s";
        private final String platform;
        private final String serverUuid;
        private final int serviceId;
        private final Consumer<JsonObjectBuilder> appendPlatformDataConsumer;
        private final Consumer<JsonObjectBuilder> appendServiceDataConsumer;
        private final Consumer<Runnable> submitTaskConsumer;
        private final Supplier<Boolean> checkServiceEnabledSupplier;
        private final BiConsumer<String, Throwable> errorLogger;
        private final Consumer<String> infoLogger;
        private final boolean logErrors;
        private final boolean logSentData;
        private final boolean logResponseStatusText;
        private final Set<CustomChart> customCharts;
        private final boolean enabled;
        
        public MetricsBase(final String platform, final String serverUuid, final int serviceId, final boolean enabled, final Consumer<JsonObjectBuilder> appendPlatformDataConsumer, final Consumer<JsonObjectBuilder> appendServiceDataConsumer, final Consumer<Runnable> submitTaskConsumer, final Supplier<Boolean> checkServiceEnabledSupplier, final BiConsumer<String, Throwable> errorLogger, final Consumer<String> infoLogger, final boolean logErrors, final boolean logSentData, final boolean logResponseStatusText) {
            this.customCharts = new HashSet<CustomChart>();
            this.platform = platform;
            this.serverUuid = serverUuid;
            this.serviceId = serviceId;
            this.enabled = enabled;
            this.appendPlatformDataConsumer = appendPlatformDataConsumer;
            this.appendServiceDataConsumer = appendServiceDataConsumer;
            this.submitTaskConsumer = submitTaskConsumer;
            this.checkServiceEnabledSupplier = checkServiceEnabledSupplier;
            this.errorLogger = errorLogger;
            this.infoLogger = infoLogger;
            this.logErrors = logErrors;
            this.logSentData = logSentData;
            this.logResponseStatusText = logResponseStatusText;
            this.checkRelocation();
            if (enabled) {
                this.startSubmitting();
            }
        }
        
        private static byte[] compress(final String str) throws IOException {
            if (str == null) {
                return null;
            }
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (final GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            }
            return outputStream.toByteArray();
        }
        
        public void addCustomChart(final CustomChart chart) {
            this.customCharts.add(chart);
        }
        
        private void startSubmitting() {
            final Runnable submitTask = () -> {
                if (!this.enabled || !this.checkServiceEnabledSupplier.get()) {
                    MetricsBase.scheduler.shutdown();
                    return;
                }
                else {
                    if (this.submitTaskConsumer != null) {
                        this.submitTaskConsumer.accept(this::submitData);
                    }
                    else {
                        this.submitData();
                    }
                    return;
                }
            };
            final long initialDelay = (long)(60000.0 * (3.0 + Math.random() * 3.0));
            final long secondDelay = (long)(60000.0 * (Math.random() * 30.0));
            MetricsBase.scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
            MetricsBase.scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1800000L, TimeUnit.MILLISECONDS);
        }
        
        private void submitData() {
            final JsonObjectBuilder baseJsonBuilder = new JsonObjectBuilder();
            this.appendPlatformDataConsumer.accept(baseJsonBuilder);
            final JsonObjectBuilder serviceJsonBuilder = new JsonObjectBuilder();
            this.appendServiceDataConsumer.accept(serviceJsonBuilder);
            final JsonObjectBuilder.JsonObject[] chartData = this.customCharts.stream().map(customChart -> customChart.getRequestJsonObject(this.errorLogger, this.logErrors)).filter(Objects::nonNull).toArray(JsonObjectBuilder.JsonObject[]::new);
            serviceJsonBuilder.appendField("id", this.serviceId);
            serviceJsonBuilder.appendField("customCharts", chartData);
            baseJsonBuilder.appendField("service", serviceJsonBuilder.build());
            baseJsonBuilder.appendField("serverUUID", this.serverUuid);
            baseJsonBuilder.appendField("metricsVersion", "2.2.1");
            final JsonObjectBuilder.JsonObject data = baseJsonBuilder.build();
            MetricsBase.scheduler.execute(() -> {
                try {
                    this.sendData(data);
                }
                catch (final Exception e) {
                    if (this.logErrors) {
                        this.errorLogger.accept("Could not submit bStats metrics data", e);
                    }
                }
            });
        }
        
        private void sendData(final JsonObjectBuilder.JsonObject data) throws Exception {
            if (this.logSentData) {
                this.infoLogger.accept("Sent bStats metrics data: " + data.toString());
            }
            final String url = String.format("https://bStats.org/api/v2/data/%s", this.platform);
            final HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();
            final byte[] compressedData = compress(data.toString());
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Connection", "close");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Metrics-Service/1");
            connection.setDoOutput(true);
            try (final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(compressedData);
            }
            final StringBuilder builder = new StringBuilder();
            try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
            }
            if (this.logResponseStatusText) {
                this.infoLogger.accept("Sent data to bStats and received response: " + (Object)builder);
            }
        }
        
        private void checkRelocation() {
            if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
                final String defaultPackage = new String(new byte[] { 111, 114, 103, 46, 98, 115, 116, 97, 116, 115 });
                final String examplePackage = new String(new byte[] { 121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101 });
                if (MetricsBase.class.getPackage().getName().startsWith(defaultPackage) || MetricsBase.class.getPackage().getName().startsWith(examplePackage)) {
                    throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
                }
            }
        }
        
        static {
            scheduler = Executors.newScheduledThreadPool(1, task -> new Thread(task, "bStats-Metrics"));
        }
    }
    
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
    
    public static class SimpleBarChart extends CustomChart
    {
        private final Callable<Map<String, Integer>> callable;
        
        public SimpleBarChart(final String chartId, final Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }
        
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            final JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                valuesBuilder.appendField(entry.getKey(), new int[] { entry.getValue() });
            }
            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
    }
    
    public static class MultiLineChart extends CustomChart
    {
        private final Callable<Map<String, Integer>> callable;
        
        public MultiLineChart(final String chartId, final Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }
        
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            final JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean allSkipped = true;
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
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
    
    public static class AdvancedPie extends CustomChart
    {
        private final Callable<Map<String, Integer>> callable;
        
        public AdvancedPie(final String chartId, final Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }
        
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            final JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean allSkipped = true;
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
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
    
    public static class JsonObjectBuilder
    {
        private StringBuilder builder;
        private boolean hasAtLeastOneField;
        
        public JsonObjectBuilder() {
            this.builder = new StringBuilder();
            this.hasAtLeastOneField = false;
            this.builder.append("{");
        }
        
        private static String escape(final String value) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < value.length(); ++i) {
                final char c = value.charAt(i);
                if (c == '\"') {
                    builder.append("\\\"");
                }
                else if (c == '\\') {
                    builder.append("\\\\");
                }
                else if (c <= '\u000f') {
                    builder.append("\\u000").append(Integer.toHexString(c));
                }
                else if (c <= '\u001f') {
                    builder.append("\\u00").append(Integer.toHexString(c));
                }
                else {
                    builder.append(c);
                }
            }
            return builder.toString();
        }
        
        public JsonObjectBuilder appendNull(final String key) {
            this.appendFieldUnescaped(key, "null");
            return this;
        }
        
        public JsonObjectBuilder appendField(final String key, final String value) {
            if (value == null) {
                throw new IllegalArgumentException("JSON value must not be null");
            }
            this.appendFieldUnescaped(key, "\"" + escape(value) + "\"");
            return this;
        }
        
        public JsonObjectBuilder appendField(final String key, final int value) {
            this.appendFieldUnescaped(key, String.valueOf(value));
            return this;
        }
        
        public JsonObjectBuilder appendField(final String key, final JsonObject object) {
            if (object == null) {
                throw new IllegalArgumentException("JSON object must not be null");
            }
            this.appendFieldUnescaped(key, object.toString());
            return this;
        }
        
        public JsonObjectBuilder appendField(final String key, final String[] values) {
            if (values == null) {
                throw new IllegalArgumentException("JSON values must not be null");
            }
            final String escapedValues = Arrays.stream(values).map(value -> "\"" + escape(value) + "\"").collect((Collector<? super Object, ?, String>)Collectors.joining(","));
            this.appendFieldUnescaped(key, "[" + escapedValues + "]");
            return this;
        }
        
        public JsonObjectBuilder appendField(final String key, final int[] values) {
            if (values == null) {
                throw new IllegalArgumentException("JSON values must not be null");
            }
            final String escapedValues = Arrays.stream(values).mapToObj((IntFunction<?>)String::valueOf).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
            this.appendFieldUnescaped(key, "[" + escapedValues + "]");
            return this;
        }
        
        public JsonObjectBuilder appendField(final String key, final JsonObject[] values) {
            if (values == null) {
                throw new IllegalArgumentException("JSON values must not be null");
            }
            final String escapedValues = Arrays.stream(values).map((Function<? super JsonObject, ?>)JsonObject::toString).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
            this.appendFieldUnescaped(key, "[" + escapedValues + "]");
            return this;
        }
        
        private void appendFieldUnescaped(final String key, final String escapedValue) {
            if (this.builder == null) {
                throw new IllegalStateException("JSON has already been built");
            }
            if (key == null) {
                throw new IllegalArgumentException("JSON key must not be null");
            }
            if (this.hasAtLeastOneField) {
                this.builder.append(",");
            }
            this.builder.append("\"").append(escape(key)).append("\":").append(escapedValue);
            this.hasAtLeastOneField = true;
        }
        
        public JsonObject build() {
            if (this.builder == null) {
                throw new IllegalStateException("JSON has already been built");
            }
            final JsonObject object = new JsonObject(this.builder.append("}").toString());
            this.builder = null;
            return object;
        }
        
        public static class JsonObject
        {
            private final String value;
            
            private JsonObject(final String value) {
                this.value = value;
            }
            
            @Override
            public String toString() {
                return this.value;
            }
        }
    }
}
