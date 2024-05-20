package hexlet.code.util;

import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private UserRepository userRepository;

    public boolean isCurrentUser(long userId) {
        var email = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return email.equals(authentication.getName());
    }

//    public boolean isUserAuthenticated() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication == null || !authentication.isAuthenticated();
//    }
}
