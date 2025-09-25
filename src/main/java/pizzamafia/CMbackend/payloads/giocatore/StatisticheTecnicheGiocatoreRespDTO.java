package pizzamafia.CMbackend.payloads.giocatore;

public record StatisticheTecnicheGiocatoreRespDTO(
        //TECNICHE
        Integer tecnica,
        Integer equilibrio,
        Integer colpoDiTesta,
        Integer tiro,
        Integer assist,
        Integer finalizzazione,
        Integer dribbling,
        Integer visione,
        Integer calciPiazzati,
        Integer cross,
        Integer contrasti,
        Integer marcatura,
        Integer intercettazione,

        //MENTALI
        Integer carisma,
        Integer concentrazione,
        Integer coraggio,
        Integer leadership,
        Integer letturaDelGioco,
        Integer giocoDiSquadra,
        Integer creativita,
        Integer freddezza,
        Integer aggressivita,

        //FISICHE
        Integer accelerazione,
        Integer scatto,
        Integer elevazione,
        Integer forzaFisica,
        Integer Resistenza,

        //PORTIERE
        Integer tuffo,
        Integer riflessi,
        Integer posizione,
        Integer uscite,
        Integer presa



        ) {}
