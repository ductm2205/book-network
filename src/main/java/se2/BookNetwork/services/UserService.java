package se2.BookNetwork.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.constants.Roles;
import se2.BookNetwork.core.requests.user.ChangePasswordRequest;
import se2.BookNetwork.core.requests.user.RegistrationRequest;
import se2.BookNetwork.core.requests.user.UpdateProfileRequest;
import se2.BookNetwork.interfaces.IFavouriteService;
import se2.BookNetwork.interfaces.IUserService;
import se2.BookNetwork.models.common.User;
import se2.BookNetwork.repositories.RoleRepository;
import se2.BookNetwork.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IFavouriteService favouriteService;

    @Override
    @Transactional
    public User registerUser(RegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }

        var role = roleRepository.findByName(Roles.USER)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(true)
                .isAccountLocked(false)
                .roles(List.of(role))
                .build();

        userRepository.save(user);
        favouriteService.createFavourite(user);

        return user;
    }

    @Override
    @Transactional

    public User updateUserProfile(UpdateProfileRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(Integer userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void assignRoleToUser(Integer userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        if (user.getRoles().contains(role)) {
            return;
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Integer userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void setAccountLocked(Integer userId, boolean lock) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setAccountLocked(lock);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void setAccountEnabled(Integer userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    public PageResponse<User> getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("firstName").ascending());
        var users = userRepository.getPaginatedUsersList(pageable);
        var usersResponse = users.stream().toList();
        return new PageResponse<>(
                usersResponse,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast());
    }

}
