package pizzamafia.CMbackend.payloads.squadra;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record NewSquadraDTO(

        // Nome obbligatorio 3-15 caratteri
        @NotBlank(message = "Il nome della squadra è obbligatorio.")
        @Size(min = 3, max = 15, message = "Il nome deve essere tra 3 e 15 caratteri.")
        String nome,

        // Colori sociali obbligatori
        @NotBlank(message = "I colori sociali sono obbligatori.")
        String coloriSociali,

        // Maglia primaria
        @NotBlank(message = "Il colore primario è obbligatorio.")
        String magliaColorePrimario,

        // Maglia secondaria
        @NotBlank(message = "Il colore secondario è obbligatorio.")
        String magliaColoreSecondario,

        // ID dello stadio associato (FK)
        @NotNull(message = "Lo stadio è obbligatorio.")
        UUID stadioId

) {}
