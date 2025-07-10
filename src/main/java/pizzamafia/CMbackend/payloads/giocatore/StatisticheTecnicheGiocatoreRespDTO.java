package pizzamafia.CMbackend.payloads.giocatore;

public record StatisticheTecnicheGiocatoreRespDTO(
        Integer attacco,
        Integer difesa,
        Integer velocita,
        Integer tiro,
        Integer passaggio,
        Integer portiere
) {}
