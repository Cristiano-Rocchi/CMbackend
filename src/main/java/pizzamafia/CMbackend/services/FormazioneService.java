package pizzamafia.CMbackend.services;



import pizzamafia.CMbackend.payloads.partita.FormazioneRespDTO;
import pizzamafia.CMbackend.payloads.partita.NewFormazioneDTO;

import java.util.List;
import java.util.UUID;

public interface FormazioneService {

    FormazioneRespDTO create(NewFormazioneDTO dto);

    FormazioneRespDTO findById(UUID id);

    List<FormazioneRespDTO> findAll();

    void deleteById(UUID id);

}

