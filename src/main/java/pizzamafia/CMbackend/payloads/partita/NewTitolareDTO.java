package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.Ruolo;

import java.util.UUID;

public record NewTitolareDTO(
        UUID giocatoreId,
        Ruolo ruolo
) {}
