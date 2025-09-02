package pizzamafia.CMbackend.payloads.giocatore;

import pizzamafia.CMbackend.enums.Ruolo;

import java.util.UUID;

public record GiocatoreLiteDTO(
        UUID id,
        String nome,
        String cognome,
        Ruolo ruolo,
        int valoreTecnico
) {}

