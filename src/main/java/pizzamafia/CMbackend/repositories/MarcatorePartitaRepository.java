package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzamafia.CMbackend.entities.MarcatorePartita;

import java.util.UUID;

public interface MarcatorePartitaRepository extends JpaRepository<MarcatorePartita, UUID> {
}
