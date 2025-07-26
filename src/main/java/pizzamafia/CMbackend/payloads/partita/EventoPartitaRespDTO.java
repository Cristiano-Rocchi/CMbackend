package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.UUID;


public record EventoPartitaRespDTO(
        UUID id,
        int minuto,
        TipoEvento tipoEvento,
        String esito,
        String note,
        UUID partitaId,
        UUID giocatorePrincipaleId,
        String nomeGiocatorePrincipale,
        UUID giocatoreSecondarioId,
        String nomeGiocatoreSecondario,
        UUID squadraId

) {}

