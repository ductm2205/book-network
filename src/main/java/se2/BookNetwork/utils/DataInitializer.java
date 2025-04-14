package se2.BookNetwork.utils;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.constants.Roles;
import se2.BookNetwork.models.common.Favourite;
import se2.BookNetwork.models.common.Role;
import se2.BookNetwork.models.common.User;
import se2.BookNetwork.repositories.FavouriteRepository;
import se2.BookNetwork.repositories.RoleRepository;
import se2.BookNetwork.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FavouriteRepository favouriteRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            // Seed roles first
            if (roleRepository.findByName(Roles.USER).isEmpty()) {
                roleRepository.save(Role.builder().name(Roles.USER).build());
            }
            if (roleRepository.findByName(Roles.ADMIN).isEmpty()) {
                roleRepository.save(Role.builder().name(Roles.ADMIN).build());
            }

            // Then seed admin
            if (userRepository.findByEmail("admin@bsn.com").isEmpty()) {
                Role adminRole = roleRepository.findByName(Roles.ADMIN)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
                User admin = User.builder()
                        .firstname("System")
                        .lastname("Admin")
                        .email("admin@bsn.com")
                        .password(passwordEncoder.encode("admin123")) // secure this!
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .isEnabled(true)
                        .isAccountLocked(false)
                        .roles(List.of(adminRole))
                        .build();
                userRepository.save(admin);

                var favourite = Favourite
                        .builder()
                        .owner(admin)
                        .createdBy(admin.getEmail())
                        .name(admin.getUsername())
                        .build();
                favouriteRepository.save(favourite);
            }
        };
    }

}
