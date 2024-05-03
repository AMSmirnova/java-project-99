package hexlet.code.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public boolean isUserAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || !authentication.isAuthenticated();
    }
}
