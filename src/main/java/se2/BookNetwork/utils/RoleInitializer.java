package se2.BookNetwork.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.constants.Roles;
import se2.BookNetwork.models.common.Role;
import se2.BookNetwork.repositories.RoleRepository;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(Roles.USER).isEmpty()) {
            Role userRole = Role.builder().name(Roles.USER).build();
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName(Roles.ADMIN).isEmpty()) {
            Role adminRole = Role.builder().name(Roles.ADMIN).build();
            roleRepository.save(adminRole);
        }
    }
}
