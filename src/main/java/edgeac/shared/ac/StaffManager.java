package edgeac.shared.ac;

import java.util.HashSet;
import java.util.Collection;

public class StaffManager
{
    private final Collection<String> staff;
    
    public StaffManager() {
        this.staff = new HashSet<String>();
    }
    
    public void addStaff(final String name) {
        this.staff.add(name);
    }
    
    public void removeStaff(final String name) {
        this.staff.remove(name);
    }
    
    public Collection<String> getAllStaff() {
        return this.staff;
    }
}
