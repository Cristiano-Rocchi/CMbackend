package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzamafia.CMbackend.entities.Allenatore;

import java.util.Optional;
import java.util.UUID;

public interface AllenatoreRepository extends JpaRepository<Allenatore, UUID> {
    Optional<Allenatore> findBySquadra_Id(UUID squadraId);
    boolean existsBySquadra_Id(UUID squadraId);
}
