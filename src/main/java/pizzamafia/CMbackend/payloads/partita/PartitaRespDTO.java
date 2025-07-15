package pizzamafia.CMbackend.payloads.partita;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PartitaRespDTO(
        UUID id,
        String squadraCasaNome,
        String squadraTrasfertaNome,
        int goalCasa,
        int golTrasferta,
        LocalDateTime dataOra,
        String competizione,
        List<MarcatoreRespDTO> marcatori
) {}
