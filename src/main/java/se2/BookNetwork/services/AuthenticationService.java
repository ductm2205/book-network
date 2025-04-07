package se2.BookNetwork.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.constants.Roles;
import se2.BookNetwork.core.requests.RegistrationRequest;
import se2.BookNetwork.interfaces.IAuthenticationService;
import se2.BookNetwork.models.common.User;
import se2.BookNetwork.repositories.RoleRepository;
import se2.BookNetwork.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void register(RegistrationRequest registrationRequest) {
        var roles = roleRepository.findByName(Roles.USER)
                .orElseThrow(() -> new IllegalStateException("Role " + Roles.USER + " not found"));

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        var user = User.builder()
                .firstname(registrationRequest.getFirstname())
                .lastname(registrationRequest.getLastname())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(List.of(roles))
                .isAccountLocked(false)
                .isEnabled(true)
                .build();

        userRepository.save(user);
    }

}
