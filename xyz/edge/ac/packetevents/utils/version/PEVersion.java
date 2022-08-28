package xyz.edge.ac.packetevents.utils.version;

import java.util.Arrays;

public class PEVersion
{
    private final int[] versionIntArray;
    
    public PEVersion(final int... version) {
        this.versionIntArray = version;
    }
    
    public PEVersion(final String version) {
        final String[] versionIntegers = version.split("\\.");
        final int length = versionIntegers.length;
        this.versionIntArray = new int[length];
        for (int i = 0; i < length; ++i) {
            this.versionIntArray[i] = Integer.parseInt(versionIntegers[i]);
        }
    }
    
    public int compareTo(final PEVersion version) {
        final int localLength = this.versionIntArray.length;
        final int oppositeLength = version.versionIntArray.length;
        for (int length = Math.max(localLength, oppositeLength), i = 0; i < length; ++i) {
            final int localInteger = (i < localLength) ? this.versionIntArray[i] : 0;
            final int oppositeInteger = (i < oppositeLength) ? version.versionIntArray[i] : 0;
            if (localInteger > oppositeInteger) {
                return 1;
            }
            if (localInteger < oppositeInteger) {
                return -1;
            }
        }
        return 0;
    }
    
    public boolean isNewerThan(final PEVersion version) {
        return this.compareTo(version) == 1;
    }
    
    public boolean isOlderThan(final PEVersion version) {
        return this.compareTo(version) == -1;
    }
    
    public int[] asArray() {
        return this.versionIntArray;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj instanceof PEVersion && Arrays.equals(this.versionIntArray, ((PEVersion)obj).versionIntArray);
    }
    
    public PEVersion clone() {
        try {
            return (PEVersion)super.clone();
        }
        catch (final CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.versionIntArray.length * 2 - 1).append(this.versionIntArray[0]);
        for (int i = 1; i < this.versionIntArray.length; ++i) {
            sb.append(".").append(this.versionIntArray[i]);
        }
        return sb.toString();
    }
}
