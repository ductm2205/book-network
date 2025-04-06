package se2.BookNetwork.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.requests.RegistrationRequest;
import se2.BookNetwork.services.AuthenticationService;

@Controller
@RequestMapping(value = "auth")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private static final String ERROR = "error";
    private static final String REGISTER_PATH = "auth/register";

    private final AuthenticationService authenticationService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("regRequest", new RegistrationRequest());
        return REGISTER_PATH;
    }

    @PostMapping("/save-user")
    public String registerUser(
            @ModelAttribute("regRequest") @Valid RegistrationRequest registrationRequest,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return REGISTER_PATH;
        }

        try {
            // Check if passwords match
            if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
                model.addAttribute(ERROR, "Passwords do not match");
                return REGISTER_PATH;
            }

            // Register the user through service
            this.authenticationService.register(registrationRequest);
            return "redirect:/auth/login?registered";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR, e.getMessage());
            return REGISTER_PATH;
        }
    }
}
