package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzamafia.CMbackend.entities.Partita;

import java.util.UUID;

public interface PartitaRepository extends JpaRepository<Partita, UUID> {
}
