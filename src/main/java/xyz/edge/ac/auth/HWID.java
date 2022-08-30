package xyz.edge.ac.auth;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HWID
{
    private static String OS;
    
    public static String getOS() {
        if (HWID.OS.isEmpty()) {
            HWID.OS = System.getProperty("os.name");
        }
        return HWID.OS;
    }
    
    public static boolean isUnix() {
        return getOS().contains("nux") || getOS().contains("nix");
    }
    
    public static String getHwid() {
        try {
            String linuxPadding = "";
            if (isUnix()) {
                linuxPadding += "unix";
                try {
                    final Runtime rt = Runtime.getRuntime();
                    final String[] commands = { "/usr/bin/hostnamectl" };
                    final Process proc = rt.exec(commands);
                    final BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    String s = null;
                    final StringBuilder full = new StringBuilder();
                    while ((s = stdInput.readLine()) != null) {
                        full.append(s).append("\n");
                    }
                    if (full.toString().contains("Machine ID: ")) {
                        linuxPadding += full.toString().split("Machine ID: ")[1].split("\n")[0];
                    }
                }
                catch (final Exception ex) {}
            }
            else {
                linuxPadding = getOS();
            }
            final String hwid = SHA1(System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name") + linuxPadding);
            return hwid;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String SHA1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
    
    private static String convertToHex(final byte[] data) {
        final StringBuffer buf = new StringBuffer();
        for (final byte datum : data) {
            int halfbyte = datum >>> 4 & 0xF;
            int var4 = 0;
            do {
                if (halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                }
                else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }
                halfbyte = (datum & 0xF);
            } while (var4++ < 1);
        }
        return buf.toString();
    }
    
    static {
        HWID.OS = "";
    }
}
