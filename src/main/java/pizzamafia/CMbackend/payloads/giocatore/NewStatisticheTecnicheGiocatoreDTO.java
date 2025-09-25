package pizzamafia.CMbackend.payloads.giocatore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewStatisticheTecnicheGiocatoreDTO(

        //=========TECNICHE=======

        @NotNull @Min(1) @Max(99)
        Integer tecnica,

        @NotNull @Min(1) @Max(99)
        Integer equilibrio,

        @NotNull @Min(1) @Max(99)
        Integer colpoDiTesta,

        @NotNull @Min(1) @Max(99)
        Integer tiro,

        @NotNull @Min(1) @Max(99)
        Integer assist,

        @NotNull @Min(1) @Max(99)
        Integer finalizzazione,

        @NotNull @Min(1) @Max(99)
        Integer dribbling,

        @NotNull @Min(1) @Max(99)
        Integer visione,

        @NotNull @Min(1) @Max(99)
        Integer calciPiazzati,

        @NotNull @Min(1) @Max(99)
        Integer cross,

        @NotNull @Min(1) @Max(99)
        Integer contrasti,

        @NotNull @Min(1) @Max(99)
        Integer marcatura,

        @NotNull @Min(1) @Max(99)
        Integer intercettazione,

        //=========MENTALI=======

        @NotNull @Min(1) @Max(99)
        Integer carisma,

        @NotNull @Min(1) @Max(99)
        Integer concentrazione,

        @NotNull @Min(1) @Max(99)
        Integer coraggio,

        @NotNull @Min(1) @Max(99)
        Integer leadership,

        @NotNull @Min(1) @Max(99)
        Integer letturaDelGioco,

        @NotNull @Min(1) @Max(99)
        Integer giocoDiSquadra,

        @NotNull @Min(1) @Max(99)
        Integer creativita,

        @NotNull @Min(1) @Max(99)
        Integer freddezza,

        @NotNull @Min(1) @Max(99)
        Integer aggressivita,

        //=========FISICHE========

        @NotNull @Min(1) @Max(99)
        Integer accelerazione,

        @NotNull @Min(1) @Max(99)
        Integer scatto,

        @NotNull @Min(1) @Max(99)
        Integer elevazione,

        @NotNull @Min(1) @Max(99)
        Integer forzaFisica,

        @NotNull @Min(1) @Max(99)
        Integer resistenza,

        //=========PORTIERE=========
        @NotNull @Min(1) @Max(99)
        Integer tuffo,

        @NotNull @Min(1) @Max(99)
        Integer riflessi,

        @NotNull @Min(1) @Max(99)
        Integer posizione,

        @NotNull @Min(1) @Max(99)
        Integer uscite,

        @NotNull @Min(1) @Max(99)
        Integer presa







) {}
