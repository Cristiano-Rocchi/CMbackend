package pizzamafia.CMbackend.services;

import pizzamafia.CMbackend.entities.Partita;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartitaService {

    Partita save(Partita partita);

    Optional<Partita> findById(UUID id);

    List<Partita> findAll();

    void deleteById(UUID id);

    Partita simulaPartita(UUID id);


}
