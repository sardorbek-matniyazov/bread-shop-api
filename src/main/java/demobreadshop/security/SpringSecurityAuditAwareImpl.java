package demobreadshop.security;

import demobreadshop.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (
                authentication != null
                        && authentication.isAuthenticated()
                        && !authentication.getPrincipal().equals("anonymousUser")
        ) return Optional.of(((User) authentication.getPrincipal()).getFullName());
        return Optional.of("test");
    }
}
