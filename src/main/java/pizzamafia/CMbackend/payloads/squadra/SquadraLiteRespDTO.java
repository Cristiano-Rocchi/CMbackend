package pizzamafia.CMbackend.payloads.squadra;

import java.util.UUID;

public record SquadraLiteRespDTO(
        UUID id,
        String nome,
        String coloriSociali,
        String magliaColorePrimario,
        String magliaColoreSecondario,
        String nomeStadio,
        Integer valoreTecnicoTotale
) {}
