package pizzamafia.CMbackend.repositories;

import pizzamafia.CMbackend.entities.Stadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StadioRepository extends JpaRepository<Stadio, UUID> {
    boolean existsByNome(String nome);
}
