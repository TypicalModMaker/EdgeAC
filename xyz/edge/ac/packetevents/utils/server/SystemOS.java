package xyz.edge.ac.packetevents.utils.server;

public enum SystemOS
{
    WINDOWS, 
    MACOS, 
    LINUX, 
    OTHER;
    
    private static final SystemOS[] VALUES;
    private static SystemOS value;
    
    public static SystemOS getOSNoCache() {
        final String os = System.getProperty("os.name").toLowerCase();
        for (final SystemOS sysos : SystemOS.VALUES) {
            if (os.contains(sysos.name().toLowerCase())) {
                return sysos;
            }
        }
        return SystemOS.OTHER;
    }
    
    public static SystemOS getOS() {
        if (SystemOS.value == null) {
            SystemOS.value = getOSNoCache();
        }
        return SystemOS.value;
    }
    
    @Deprecated
    public static SystemOS getOperatingSystem() {
        return getOS();
    }
    
    static {
        VALUES = values();
    }
}
