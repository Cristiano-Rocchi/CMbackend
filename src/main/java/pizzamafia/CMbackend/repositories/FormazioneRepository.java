package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzamafia.CMbackend.entities.Formazione;

import java.util.List;
import java.util.UUID;

public interface FormazioneRepository extends JpaRepository<Formazione, UUID> {
    List<Formazione> findByPartitaId(UUID partitaId);
}
