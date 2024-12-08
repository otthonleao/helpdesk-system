package model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record AuthenticateRequest(

        @Schema(description = "Email of the user", example = "otthon@mail.com")
        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required and can't be empty")
        @Size(min = 5, max = 50, message = "Email must contain between 5 and 50 characters")
        String email,

        @Schema(description = "Password of the user", example = "senha123")
        @NotBlank(message = "Password is required and can't be empty")
        @Size(min = 6, max = 30, message = "Password must contain between 6 and 30 characters")
        String password
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
