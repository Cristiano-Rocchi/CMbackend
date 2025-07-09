package pizzamafia.CMbackend.payloads.stadio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewStadioDTO(

        // Nome obbligatorio e unico 3-20 caratteri
        @NotBlank(message = "Il nome dello stadio è obbligatorio.")
        @Size(min = 3, max = 20, message = "Il nome dello stadio deve essere tra 3 e 20 caratteri.")
        String nome,

        // Luogo obbligatorio 3-20 caratteri
        @NotBlank(message = "Il luogo è obbligatorio.")
        @Size(min = 3, max = 20, message = "Il luogo deve essere tra 3 e 20 caratteri.")
        String luogo,

        // Capienza minima di 1.000
        @Min(value = 1000, message = "La capienza deve essere almeno di 1.000 posti.")
        Long capienza

) {}
