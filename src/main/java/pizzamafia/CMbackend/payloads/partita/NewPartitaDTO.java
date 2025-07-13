package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.Competizione;

import java.time.LocalDateTime;
import java.util.UUID;

public record NewPartitaDTO(
        UUID squadraCasaId,
        UUID squadraTrasfertaId,
        Competizione competizione,
        LocalDateTime dataOra
) {}

