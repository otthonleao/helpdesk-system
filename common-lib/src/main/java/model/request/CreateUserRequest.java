package model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.With;
import model.enums.ProfileEnum;
import java.util.Set;

@With
public record CreateUserRequest(

        @NotBlank(message = "Name is required and can't be empty")
        @Schema(description = "Name of the user", example = "Otthon Leão")
        @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters")
        String name,

        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required and can't be empty")
        @Schema(description = "Email of the user", example = "meu_email@mail.com")
        @Size(min = 5, max = 50, message = "Email must contain between 5 and 50 characters")
        String email,

        @NotBlank(message = "Password is required and can't be empty")
        @Schema(description = "Password of the user", example = "123456")
        @Size(min = 6, max = 30, message = "Password must contain between 6 and 30 characters")
        String password,

        @Schema(description = "Profiles of the user", example = "[\"ROLE_ADMIN\", \"ROLE_CUSTOMER\"]")
        Set<ProfileEnum> profiles

) {
}
