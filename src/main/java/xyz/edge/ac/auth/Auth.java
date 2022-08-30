package xyz.edge.ac.auth;

import xyz.edge.ac.Edge;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import xyz.edge.ac.Error;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import xyz.edge.ac.util.CustomConfig;
import xyz.edge.api.license.LicenseType;
import java.util.function.Consumer;
import java.net.URL;

public final class Auth
{
    private static final String AUTH_URL = "https://catto.cc";
    private static URL apiUrl;
    private static String users;
    
    public static void requestLicenseType(final Consumer<LicenseType> response) {
        response.accept(LicenseType.DEVELOPMENT);
    }
    
    public static void sendRequest() {
        try {
            final byte[] urlParamsBytes = ("key=" + CustomConfig.getAuthKey()).getBytes(StandardCharsets.UTF_8);
            final int postDataParamLength = urlParamsBytes.length;
            final URL url = new URL("https://catto.cc/applications/nexus/interface/licenses/?info");
            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            try (final DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(urlParamsBytes);
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final JsonElement parser = new JsonParser().parse(in);
            final JsonObject json = parser.getAsJsonObject();
            final String customerName = json.get("customer_name").getAsString();
            final String email = json.get("customer_email").getAsString();
            final String productName = json.get("purchase_name").getAsString();
            final int purchaseActive = json.get("purchase_active").getAsInt();
            final long productEndDate = getJsonLong(json, "purchase_expire");
            Edge.getInstance().setPurchaseActive(purchaseActive);
            Edge.getInstance().setLicenceEmail(email);
            Edge.getInstance().setLicenceUsername(customerName);
            Edge.getInstance().setExpireDate(productEndDate);
            Edge.getInstance().setProduct(productName);
            connection.disconnect();
        }
        catch (final Exception e) {
            Error.sendAuthError();
        }
    }
    
    public static long getJsonLong(final JsonObject jsonObject, final String key) {
        if (!jsonObject.has(key)) {
            return -1L;
        }
        try {
            return jsonObject.get(key).getAsLong();
        }
        catch (final UnsupportedOperationException e) {
            return -1L;
        }
    }
    
    private Auth() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
