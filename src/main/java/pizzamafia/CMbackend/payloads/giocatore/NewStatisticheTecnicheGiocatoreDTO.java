package pizzamafia.CMbackend.payloads.giocatore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record NewStatisticheTecnicheGiocatoreDTO(

        //FISICI
        @NotNull @Min(1) @Max(99)
        Integer accelerazione,

        @NotNull @Min(1) @Max(99)
        Integer agilita,

        @NotNull @Min(1) @Max(99)
        Integer elevazione,

        @NotNull @Min(1) @Max(99)
        Integer forza,

        @NotNull @Min(1) @Max(99)
        Integer resistenza,

        @NotNull @Min(1) @Max(99)
        Integer scatto,

        @NotNull @Min(1) @Max(99)
        Integer inserimento,

        //MENTALI
        @NotNull @Min(1) @Max(99)
        Integer aggressivita,

        @NotNull @Min(1) @Max(99)
        Integer carisma,

        @NotNull @Min(1) @Max(99)
        Integer coraggio,

        @NotNull @Min(1) @Max(99)
        Integer creativita,

        @NotNull @Min(1) @Max(99)
        Integer determinazione,

        @NotNull @Min(1) @Max(99)
        Integer giocoDiSquadra,

        @NotNull @Min(1) @Max(99)
        Integer impegno,

        @NotNull @Min(1) @Max(99)
        Integer intuito,

        @NotNull @Min(1) @Max(99)
        Integer posizione,

        //TECNICI
        @NotNull @Min(1) @Max(99)
        Integer calciPiazzati,

        @NotNull @Min(1) @Max(99)
        Integer colpoDiTesta,

        @NotNull @Min(1) @Max(99)
        Integer contrasti,

        @NotNull @Min(1) @Max(99)
        Integer dribbling,

        @NotNull @Min(1) @Max(99)
        Integer finalizazione,

        @NotNull @Min(1) @Max(99)
        Integer marcatura,

        @NotNull @Min(1) @Max(99)
        Integer riflessi,

        @NotNull @Min(1) @Max(99)
        Integer tecnica,

        @NotNull @Min(1) @Max(99)
        Integer assist,

        @NotNull @Min(1) @Max(99)
        Integer tiriDaLontano

) {}
