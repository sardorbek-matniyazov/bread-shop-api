package demobreadshop.security;

import demobreadshop.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (
                authentication != null
                        && authentication.isAuthenticated()
                        && !authentication.getPrincipal().equals("anonymousUser")
        ) return Optional.of(((User) authentication.getPrincipal()).getId());
        return Optional.empty();
    }
}
