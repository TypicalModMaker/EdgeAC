package xyz.edge.ac.util;

import java.util.List;
import java.util.ArrayList;

public final class ClientHandler
{
    public static boolean process(final String payload) {
        if (payload == null) {
            return false;
        }
        if (payload.length() > 30) {
            return false;
        }
        final List<String> blacklisted = new ArrayList<String>();
        blacklisted.add("\n");
        blacklisted.add(" ");
        blacklisted.add("\\u");
        blacklisted.add("&");
        return !payload.contains(blacklisted.toString());
    }
    
    private ClientHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
