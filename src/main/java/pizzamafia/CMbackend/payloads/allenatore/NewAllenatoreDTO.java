package pizzamafia.CMbackend.payloads.allenatore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pizzamafia.CMbackend.enums.IdeaDiGioco;

import java.util.UUID;

public record NewAllenatoreDTO(
        @NotBlank String nome,
        @NotBlank String cognome,
        @NotNull IdeaDiGioco ideaDiGioco,
        @NotNull UUID squadraId
) {
}
