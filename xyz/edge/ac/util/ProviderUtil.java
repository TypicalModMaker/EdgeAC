package xyz.edge.ac.util;

import org.bukkit.entity.Player;

public final class ProviderUtil
{
    public static String getProvider(final Player player) {
        final String provider = player.getAddress().getAddress().getHostName();
        String translatedProvider = "Unknown";
        if (provider.contains("telecomitalia.it")) {
            translatedProvider = "Tim Italy";
        }
        if (provider.contains("btcentralplus.com") || provider.contains("bt.com") || provider.contains("bt")) {
            translatedProvider = "BT (British Telecom)";
        }
        if (provider.contains("comcast.net")) {
            translatedProvider = "Comcast USA";
        }
        if (provider.contains("cattoshield.com")) {
            translatedProvider = "CattoShield";
        }
        if (provider.contains("telenet.be")) {
            translatedProvider = "Telenet Belgium";
        }
        if (provider.contains("kpn.net")) {
            translatedProvider = "KPN Netherlands";
        }
        if (provider.contains("rr.com")) {
            translatedProvider = "Spectrum";
        }
        if (provider.contains("kabel-deutschland.de")) {
            translatedProvider = "Vodafone Germany";
        }
        if (provider.contains("sky.com") || provider.contains("bb.sky.com")) {
            translatedProvider = "Sky UK";
        }
        if (provider.contains("skybroadband.com")) {
            translatedProvider = "Sky Broadband";
        }
        if (provider.contains("eircom.net") || provider.contains("qkr.eircom.net")) {
            translatedProvider = "EIR Ireland";
        }
        if (provider.contains("ee.co.uk") || provider.contains("ee.com")) {
            translatedProvider = "EE UK";
        }
        if (provider.contains("splius.lt")) {
            translatedProvider = "Splius Lithuania";
        }
        if (provider.contains("sbcglobal.net")) {
            translatedProvider = "AT&T USA";
        }
        return translatedProvider;
    }
    
    private ProviderUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
