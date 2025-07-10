package pizzamafia.CMbackend.payloads.giocatore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewStatisticheTecnicheGiocatoreDTO(

        @NotNull @Min(1) @Max(99)
        Integer attacco,

        @NotNull @Min(1) @Max(99)
        Integer difesa,

        @NotNull @Min(1) @Max(99)
        Integer velocita,

        @NotNull @Min(1) @Max(99)
        Integer tiro,

        @NotNull @Min(1) @Max(99)
        Integer passaggio,

        @NotNull @Min(1) @Max(99)
        Integer portiere

) {}
