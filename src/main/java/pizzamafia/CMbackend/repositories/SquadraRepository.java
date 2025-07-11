package pizzamafia.CMbackend.repositories;

import pizzamafia.CMbackend.entities.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SquadraRepository extends JpaRepository<Squadra, UUID> {

    boolean existsByNome(String nome);
    //cerca per nome
    List<Squadra> findByNomeContainingIgnoreCase(String nome);

}
