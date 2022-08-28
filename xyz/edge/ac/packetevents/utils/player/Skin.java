package xyz.edge.ac.packetevents.utils.player;

public class Skin
{
    private String value;
    private String signature;
    
    public Skin(final String value, final String signature) {
        this.value = value;
        this.signature = signature;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public String getSignature() {
        return this.signature;
    }
    
    public void setSignature(final String signature) {
        this.signature = signature;
    }
}
