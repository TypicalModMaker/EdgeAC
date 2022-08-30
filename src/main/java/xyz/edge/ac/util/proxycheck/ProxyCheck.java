package xyz.edge.ac.util.proxycheck;

import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.InetAddress;

public final class ProxyCheck
{
    public static String getCountry(final InetAddress ip) {
        try {
            final URL url = new URL("http://ip-api.com/json/" + ip.getHostAddress());
            String entirePage;
            try (final BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()))) {
                entirePage = stream.lines().collect(Collectors.joining());
            }
            return entirePage.contains("\"country\":\"") ? entirePage.split("\"country\":\"")[1].split("\",")[0] : null;
        }
        catch (final Exception ignored) {
            return null;
        }
    }
    
    public static String getRegionName(final InetAddress ip) {
        try {
            String entirePage;
            try (final BufferedReader stream = new BufferedReader(new InputStreamReader(new URL("http://ip-api.com/json/" + ip.getHostAddress()).openStream()))) {
                entirePage = stream.lines().collect(Collectors.joining());
                stream.close();
            }
            return entirePage.contains("\"regionName\":\"") ? entirePage.split("\"regionName\":\"")[1].split("\",")[0] : null;
        }
        catch (final Exception ignored) {
            return null;
        }
    }
    
    @Deprecated
    public static String getUrlAsString(final String url) {
        try {
            final URL urlObj = new URL(url);
            final URLConnection con = urlObj.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Cookie", "myCookie=test123");
            con.connect();
            String response;
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                final String newLine = System.getProperty("line.separator");
                response = "";
                in.close();
            }
            return response;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Deprecated
    public static boolean isVPN(final InetSocketAddress ip) {
        final String result = getUrlAsString("http://iphub.info/?ip=" + ip.getHostName());
        return !result.equals("Probably Home Internet");
    }
}
