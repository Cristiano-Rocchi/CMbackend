package pizzamafia.CMbackend.payloads.giocatore;

public record StatisticheTecnicheGiocatoreRespDTO(
        //FISICI
        Integer accelerazione,
        Integer agilita,
        Integer elevazione,
        Integer forza,
        Integer resistenza,
        Integer scatto,
        Integer inserimento,
        //MENTALI
        Integer aggressivita,
        Integer carisma,
        Integer coraggio,
        Integer creativita,
        Integer determinazione,
        Integer giocoDiSquadra,
        Integer impegno,
        Integer intuito,
        Integer posizione,
        //TECNICI
        Integer calciPiazzati,
        Integer colpoDiTesta,
        Integer contrasti,
        Integer dribbling,
        Integer finalizzazione,
        Integer marcatura,
        Integer riflessi,
        Integer tecnica,
        Integer assist,
        Integer tiriDaLontano


        ) {}
