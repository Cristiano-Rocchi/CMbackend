package pizzamafia.CMbackend.services;

import pizzamafia.CMbackend.payloads.allenatore.AllenatoreRespDTO;
import pizzamafia.CMbackend.payloads.allenatore.NewAllenatoreDTO;

import java.util.List;
import java.util.UUID;

public interface AllenatoreService {
    AllenatoreRespDTO create(NewAllenatoreDTO dto);
    AllenatoreRespDTO getById(UUID id);
    List<AllenatoreRespDTO> getAll();
    AllenatoreRespDTO update(UUID id, NewAllenatoreDTO dto);
    void delete(UUID id);
    AllenatoreRespDTO getBySquadraId(UUID squadraId);
}
