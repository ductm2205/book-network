package se2.BookNetwork.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.requests.user.ChangePasswordRequest;
import se2.BookNetwork.core.requests.user.RegistrationRequest;
import se2.BookNetwork.core.requests.user.UpdateProfileRequest;
import se2.BookNetwork.core.requests.user.UserUpdateRequest;
import se2.BookNetwork.interfaces.IUserService;
import se2.BookNetwork.models.common.User;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    // Show registration form
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new RegistrationRequest());
        return "users/register";
    }

    // Handle registration
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String registerUser(
            @Valid @ModelAttribute("userForm") RegistrationRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }
        if (result.hasErrors()) {
            return "users/register";
        }

        try {
            userService.registerUser(request);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
            return "users/list";
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "users/register";
        }
    }

    // Show user profile
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal User user, Model model) {
        var profile = UpdateProfileRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .build();
        model.addAttribute("profile", profile);
        model.addAttribute("title", "Profile");
        model.addAttribute("user", user);
        return "users/profile";
    }

    // Update user profile
    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("profile") UpdateProfileRequest profile,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "users/profile";
        }

        try {
            userService.updateUserProfile(profile);

            user.setFirstname(profile.getFirstName());
            user.setLastname(profile.getLastName());
            user.setEmail(profile.getEmail());

            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
            return "redirect:/users/profile";
        } catch (IllegalArgumentException e) {
            result.reject("error.user", e.getMessage());
            return "users/profile";
        }
    }

    // Show change password form
    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordForm", new ChangePasswordRequest());
        model.addAttribute("title", "Change Password");
        return "users/change-password";
    }

    // Handle password change
    @PostMapping("/change-password")
    public String changePassword(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("passwordForm") ChangePasswordRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "users/change-password";
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.password", "Passwords do not match");
            return "users/change-password";
        }

        try {
            userService.changePassword(user.getId(), request);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
            return "redirect:/users/profile";
        } catch (IllegalArgumentException e) {
            result.reject("error.password", e.getMessage());
            return "users/change-password";
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String listUsers(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            Model model) {
        PageResponse<User> users = userService.getAllUsers(pageNumber, pageSize);
        model.addAttribute("users", users.getElements());
        model.addAttribute("currentPage", users.getPageNumber());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalItems", users.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("title", "Users List");
        return "users/list";
    }

    // Show user management form (admin only)
    @GetMapping("/{userId}/manage")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showManageUserForm(@PathVariable Integer userId, Model model) {
        var user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "users/manage";
    }

    // Handle user management (admin only)
    @PostMapping("/{userId}/manage")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String manageUser(
            @PathVariable Integer userId,
            UserUpdateRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            userService.setAccountLocked(userId, request.isLock());
            userService.setAccountEnabled(userId, request.isEnable());
            var assignRole = request.getAssignRole();
            if (assignRole != null && !assignRole.isEmpty()) {
                userService.assignRoleToUser(userId, assignRole);
            }
            var removeRole = request.getRemoveRole();
            if (removeRole != null && !removeRole.isEmpty()) {
                userService.removeRoleFromUser(userId, removeRole);
            }
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users/" + userId + "/manage";
    }
}
