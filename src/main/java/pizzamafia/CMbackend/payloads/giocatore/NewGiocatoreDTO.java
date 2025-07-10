package pizzamafia.CMbackend.payloads.giocatore;

import jakarta.validation.Valid;
import pizzamafia.CMbackend.enums.Piede;
import pizzamafia.CMbackend.enums.Ruolo;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public record NewGiocatoreDTO(

        @NotBlank(message = "Il nome è obbligatorio.")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio.")
        String cognome,

        @NotNull(message = "La data di nascita è obbligatoria.")
        LocalDate dataDiNascita,

        @NotBlank(message = "La nazionalità è obbligatoria.")
        String nazionalita,

        @NotNull(message = "L'altezza è obbligatoria.")
        @Min(value = 140, message = "L'altezza minima è 140 cm.")
        @Max(value = 250, message = "L'altezza massima è 250 cm.")
        Integer altezza,

        @NotNull(message = "Il piede preferito è obbligatorio.")
        Piede piede,

        @NotNull(message = "Il ruolo è obbligatorio.")
        Ruolo ruolo,

        @NotNull(message = "La squadra è obbligatoria.")
        UUID squadraId,

        @NotNull(message = "Le statistiche sono obbligatorie.")
        @Valid
        NewStatisticheTecnicheGiocatoreDTO statisticheTecniche

) {}
