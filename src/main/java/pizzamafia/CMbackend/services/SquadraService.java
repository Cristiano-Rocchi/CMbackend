package pizzamafia.CMbackend.services;

import pizzamafia.CMbackend.payloads.squadra.NewSquadraDTO;
import pizzamafia.CMbackend.payloads.squadra.SquadraRespDTO;

import java.util.List;
import java.util.UUID;

public interface SquadraService {

    // Crea una nuova squadra
    SquadraRespDTO create(NewSquadraDTO dto);

    // Restituisce una squadra per ID
    SquadraRespDTO getById(UUID id);

    // Restituisce tutte le squadre
    List<SquadraRespDTO> getAll();

    // Modifica una squadra
    SquadraRespDTO update(UUID id, NewSquadraDTO dto);

    // Elimina una squadra
    void delete(UUID id);
}

