package library.backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginByEmailDto(
        @NotNull(message = "Expected email field.") 
        @NotBlank(message = "Email cannot be blank.") 
        String email,
        @NotNull(message = "Expected password field.") 
        @NotBlank(message = "Password cannot be blank.") 
        String password) {
}
