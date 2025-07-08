package pizzamafia.CMbackend.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserDTO(

        //Username obbligatorio (3-12 caratteri)
        @NotBlank(message = "Lo username è obbligatorio.")
        @Size(min = 3, max = 12, message = "Lo username deve essere tra 3 e 12 caratteri.")
        String username,

        //Email obbligatoria
        @NotBlank(message = "Email obbligatoria")
        @Email(message = "Inserisci una email valida")
        String email,

        //Password obbligatoria
        @NotBlank(message = "Password obbligatoria")
        String password

        ) {

}

