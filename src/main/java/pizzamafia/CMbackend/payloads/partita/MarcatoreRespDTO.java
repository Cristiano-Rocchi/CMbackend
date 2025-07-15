package pizzamafia.CMbackend.payloads.partita;

import java.util.UUID;

public record MarcatoreRespDTO(
        UUID giocatoreId,
        String nome,
        String cognome,
        String nomeSquadra,
        int minuto
) {}

