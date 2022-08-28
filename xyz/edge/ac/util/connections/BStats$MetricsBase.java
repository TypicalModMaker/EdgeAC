package xyz.edge.ac.util.connections;

import java.util.concurrent.Executors;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.concurrent.ScheduledExecutorService;

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
    
    static {
        scheduler = Executors.newScheduledThreadPool(1, task -> new Thread(task, "bStats-Metrics"));
    }
}
