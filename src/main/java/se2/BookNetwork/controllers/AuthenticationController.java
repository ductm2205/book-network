package se2.BookNetwork.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.requests.user.RegistrationRequest;
import se2.BookNetwork.services.AuthenticationService;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String REGISTER_PATH = "auth/register";

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        model.addAttribute("title", "Login");
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully");
        }
        return "auth/login";
    }

    // @PostMapping("/login")
    // public String authenticateUser(Model model) {
    // model.addAttribute("title", "Login");
    // return "login";
    // }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest", new RegistrationRequest());
        model.addAttribute("title", "Register");
        return REGISTER_PATH;
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("registerRequest") @Valid RegistrationRequest registrationRequest,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Register");
            return REGISTER_PATH;
        }

        try {
            // Check if passwords match
            if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
                result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
                model.addAttribute("title", "Register");
                return REGISTER_PATH;
            }

            // Register the user through service
            this.authenticationService.register(registrationRequest);
            return "redirect:/login?registered";

        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            model.addAttribute("title", "Register");
            return REGISTER_PATH;
        }
    }
}
