package pizzamafia.CMbackend.payloads.partita;

import java.util.UUID;

public record PanchinaRespDTO
        (
                UUID giocatoreId,
                String nome,
                String cognome

        ){}
