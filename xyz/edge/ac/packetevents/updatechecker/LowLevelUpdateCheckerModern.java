package xyz.edge.ac.packetevents.updatechecker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;

public class LowLevelUpdateCheckerModern implements LowLevelUpdateChecker
{
    @Override
    public String getLatestRelease() {
        String jsonResponse;
        try {
            jsonResponse = this.getLatestReleaseJson();
        }
        catch (final IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to parse packetevents version!");
        }
        final JsonParser parser = new JsonParser();
        final JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();
        String versionName = null;
        if (jsonObject != null) {
            versionName = jsonObject.get("name").getAsString();
        }
        return versionName;
    }
}
