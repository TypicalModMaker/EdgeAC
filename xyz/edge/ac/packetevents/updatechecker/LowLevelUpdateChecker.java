package xyz.edge.ac.packetevents.updatechecker;

import java.io.IOException;
import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public interface LowLevelUpdateChecker
{
    String getLatestRelease();
    
    default String getLatestReleaseJson() throws IOException {
        final URLConnection connection = new URL("https://api.github.com/repos/retrooper/packetevents/releases/latest").openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final String line = reader.readLine();
        reader.close();
        return line;
    }
}
