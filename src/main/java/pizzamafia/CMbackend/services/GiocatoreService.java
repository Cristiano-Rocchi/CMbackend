package pizzamafia.CMbackend.services;

import pizzamafia.CMbackend.payloads.giocatore.NewGiocatoreDTO;
import pizzamafia.CMbackend.payloads.giocatore.GiocatoreRespDTO;

import java.util.List;
import java.util.UUID;

public interface GiocatoreService {

    // Crea un nuovo giocatore
    GiocatoreRespDTO create(NewGiocatoreDTO dto);

    // Ottiene un giocatore per ID
    GiocatoreRespDTO getById(UUID id);

    // Restituisce tutti i giocatori
    List<GiocatoreRespDTO> getAll();

    //Elimina un giocatore
    void delete(UUID id);

    //Modifica un giocatore
    GiocatoreRespDTO update(UUID id, NewGiocatoreDTO dto);

    List<GiocatoreRespDTO> findAllBySquadraId(UUID id);

}

