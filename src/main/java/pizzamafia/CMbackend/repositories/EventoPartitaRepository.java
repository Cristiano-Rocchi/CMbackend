package pizzamafia.CMbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pizzamafia.CMbackend.entities.EventoPartita;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventoPartitaRepository extends JpaRepository<EventoPartita, UUID> {

    // Trova tutti gli eventi legati a una partita
    List<EventoPartita> findByPartitaId(UUID partitaId);

    // Trova tutti gli eventi di una partita per tipo
    List<EventoPartita> findByPartitaIdAndTipoEvento(UUID partitaId, TipoEvento tipoEvento);
}
