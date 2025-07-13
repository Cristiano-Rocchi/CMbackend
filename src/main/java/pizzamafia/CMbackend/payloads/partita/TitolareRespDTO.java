package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.Ruolo;

import java.util.UUID;

public record TitolareRespDTO(
        UUID giocatoreId,
        String nome,
        String cognome,
        Ruolo ruolo,
        int valoreEffettivo
) {}
