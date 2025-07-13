package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pizzamafia.CMbackend.entities.Titolari;

import java.util.UUID;

public interface TitolariRepository extends JpaRepository<Titolari, UUID> {
}
