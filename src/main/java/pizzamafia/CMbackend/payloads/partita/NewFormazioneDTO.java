package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.Modulo;

import java.util.List;
import java.util.UUID;

public record NewFormazioneDTO(
        UUID partitaId,
        UUID squadraId,
        Modulo modulo,
        List<NewTitolareDTO> titolari
) {}

