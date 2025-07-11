package pizzamafia.CMbackend.repositories;

import pizzamafia.CMbackend.entities.Giocatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GiocatoreRepository extends JpaRepository<Giocatore, UUID> {
    boolean existsByNomeAndCognome(String nome, String cognome);
    //cerca per squadra
    List<Giocatore> findBySquadraId(UUID squadraId);
    //cerca per nome o cognome
    List<Giocatore> findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(String nome, String cognome);

}
