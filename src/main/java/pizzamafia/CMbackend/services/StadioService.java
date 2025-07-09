package pizzamafia.CMbackend.services;

import pizzamafia.CMbackend.payloads.stadio.NewStadioDTO;
import pizzamafia.CMbackend.payloads.stadio.StadioRespDTO;

import java.util.List;
import java.util.UUID;

public interface StadioService {

    // Crea un nuovo stadio
    StadioRespDTO create(NewStadioDTO dto);

    // Recupera uno stadio per ID
    StadioRespDTO getById(UUID id);

    // Recupera tutti gli stadi
    List<StadioRespDTO> getAll();

    // Modifica uno stadio esistente
    StadioRespDTO update(UUID id, NewStadioDTO dto);

    // Elimina uno stadio
    void delete(UUID id);
}
