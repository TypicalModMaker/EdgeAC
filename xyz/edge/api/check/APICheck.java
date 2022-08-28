package xyz.edge.api.check;

public interface APICheck
{
    DetectionData getCheckInfo();
    
    boolean isEnabled();
    
    int getVl();
}
