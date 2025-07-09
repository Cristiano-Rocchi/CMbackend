package pizzamafia.CMbackend.payloads.stadio;

import java.util.UUID;

public record StadioRespDTO(
        UUID id,
        String nome,
        String luogo,
        Long capienza
) {}
