package pizzamafia.CMbackend.payloads.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank(message = "Username obbligatorio")
        String identifier,

        @NotBlank(message = "password obbligatoria")
        String password
) {
}
