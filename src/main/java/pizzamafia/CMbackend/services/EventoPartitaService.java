package pizzamafia.CMbackend.services;


import pizzamafia.CMbackend.payloads.partita.EventoPartitaRespDTO;

import java.util.List;
import java.util.UUID;

public interface EventoPartitaService {

    // Recupera tutti gli eventi di una determinata partita
    List<EventoPartitaRespDTO> getEventiByPartitaId(UUID partitaId);
}
