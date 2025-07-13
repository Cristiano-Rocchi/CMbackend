package pizzamafia.CMbackend.services;

import pizzamafia.CMbackend.payloads.partita.TitolareRespDTO;

import java.util.List;
import java.util.UUID;

public interface TitolariService {

    List<TitolareRespDTO> getByFormazioneId(UUID formazioneId);

    void deleteById(UUID id);

}
