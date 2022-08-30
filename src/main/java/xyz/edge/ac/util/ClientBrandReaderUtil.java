package xyz.edge.ac.util;

public final class ClientBrandReaderUtil
{
    public static String replaceAll(final String brand) {
        String translatedBrand = "Unknown";
        if (brand.contains("lunar")) {
            translatedBrand = "LunarClient";
        }
        if (brand.contains("feather")) {
            translatedBrand = "FeatherClient";
        }
        if (brand.contains("vanilla")) {
            translatedBrand = "Vanilla";
        }
        if (brand.contains("crystalclient")) {
            translatedBrand = "CrystalClient";
        }
        return translatedBrand;
    }
    
    private ClientBrandReaderUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
