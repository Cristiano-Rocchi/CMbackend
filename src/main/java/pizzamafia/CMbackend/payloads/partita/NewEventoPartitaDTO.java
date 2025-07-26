package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.UUID;

public record NewEventoPartitaDTO(
        int minuto,
        TipoEvento tipoEvento,
        String esito,
        String note,
        UUID partitaId,
        UUID giocatorePrincipaleId,
        UUID giocatoreSecondarioId,
        UUID squadraId
) {}
