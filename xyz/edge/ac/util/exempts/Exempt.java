package xyz.edge.ac.util.exempts;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Arrays;
import xyz.edge.ac.util.exempts.type.Exempts;
import xyz.edge.ac.user.User;

public final class Exempt
{
    private final User user;
    
    public boolean isExempt(final Exempts exemptType) {
        return exemptType.getException().apply(this.user);
    }
    
    public boolean isExempt(final Exempts... exemptTypes) {
        return Arrays.stream(exemptTypes).anyMatch(this::isExempt);
    }
    
    public boolean isExempt(final Function<User, Boolean> exception) {
        return exception.apply(this.user);
    }
    
    public Exempt(final User user) {
        this.user = user;
    }
}
