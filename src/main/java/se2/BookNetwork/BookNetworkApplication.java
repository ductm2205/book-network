package se2.BookNetwork;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import se2.BookNetwork.core.constants.Roles;
import se2.BookNetwork.models.common.Role;
import se2.BookNetwork.repositories.RoleRepository;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BookNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApplication.class, args);
	}

		// auto seed role "USER"
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName(Roles.USER).isEmpty()) {
				roleRepository.save(
						Role.builder().name(Roles.USER).build());
			}
		};
	}
}
