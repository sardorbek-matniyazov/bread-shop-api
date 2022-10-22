package demobreadshop.config;

import demobreadshop.domain.Role;
import demobreadshop.domain.User;
import demobreadshop.domain.enums.RoleName;
import demobreadshop.repository.RoleRepository;
import demobreadshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

public class ObjectCreator implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public ObjectCreator(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        roleCreator();
        userCreator();
    }

    private void roleCreator() {
        Arrays.stream(RoleName.values()).forEach(roleName -> {
            if (!roleRepository.existsByRoleName(roleName)) {
                roleRepository.save(
                        new Role(roleName)
                );
            }
        });
    }

    private void userCreator() {
        if (!userRepository.existsByPhoneNumber("admin")) {
            userRepository.save(
                    new User(
                            "Javlon",
                            "admin",
                            passwordEncoder.encode("admin123"),
                            Collections.singleton(roleRepository.getByRoleName(RoleName.GL_ADMIN)),
                            50.0
                    )
            );
        }
    }

}
