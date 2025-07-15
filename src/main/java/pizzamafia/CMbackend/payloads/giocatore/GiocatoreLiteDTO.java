package pizzamafia.CMbackend.payloads.giocatore;

import java.util.UUID;

public record GiocatoreLiteDTO(
        UUID id,
        String nome,
        String cognome,
        int valoreTecnico
) {}

