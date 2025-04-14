package se2.BookNetwork.core.requests.user;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "Firstname is required")
    @NotBlank(message = "Firstname is required")
    private String firstname;
    @NotEmpty(message = "Lastname is required")
    @NotBlank(message = "Lastname is required")
    private String lastname;

    @Email(message = "Wrong email format")
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have more than 8 characters")
    private String password;

    @NotEmpty(message = "Confirm password is required")
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    private LocalDate dateOfBirth;

    private String role;
    private boolean enabled;
}