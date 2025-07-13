package pizzamafia.CMbackend.payloads.partita;

import java.time.LocalDateTime;
import java.util.UUID;

public record PartitaRespDTO(
        UUID id,
        String squadraCasaNome,
        String squadraTrasfertaNome,
        int goalCasa,
        int golTrasferta,
        LocalDateTime dataOra,
        String competizione
) {}
