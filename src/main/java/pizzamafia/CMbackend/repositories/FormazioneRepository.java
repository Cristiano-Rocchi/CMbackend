package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzamafia.CMbackend.entities.Formazione;

import java.util.UUID;

public interface FormazioneRepository extends JpaRepository<Formazione, UUID> {
}
