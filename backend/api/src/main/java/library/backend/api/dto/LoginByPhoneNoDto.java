package library.backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginByPhoneNoDto(
        @NotNull(message = "Expected email field.") 
        @NotBlank(message = "Email cannot be blank.")
        @Pattern(regexp = "\\d{10}", message = "Phone number should be 10 digits")
        String phoneNo,
        @NotNull(message = "Expected password field.") 
        @NotBlank(message = "Password cannot be blank.") 
        String password) {
}
