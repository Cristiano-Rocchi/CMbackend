package pizzamafia.CMbackend.payloads.giocatore;

import pizzamafia.CMbackend.enums.Piede;
import pizzamafia.CMbackend.enums.Ruolo;

import java.time.LocalDate;
import java.util.UUID;

public record GiocatoreRespDTO(
        UUID id,
        String nome,
        String cognome,
        LocalDate dataDiNascita,
        String nazionalita,
        Integer altezza,
        Piede piede,
        Ruolo ruolo,
        Integer valoreTecnico,
        UUID squadraId,
        String nomeSquadra,
        StatisticheTecnicheGiocatoreRespDTO statisticheTecniche
) {}
