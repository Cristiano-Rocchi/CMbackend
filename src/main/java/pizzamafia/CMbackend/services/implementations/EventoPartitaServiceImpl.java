package pizzamafia.CMbackend.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.EventoPartita;
import pizzamafia.CMbackend.payloads.partita.EventoPartitaRespDTO;
import pizzamafia.CMbackend.repositories.EventoPartitaRepository;
import pizzamafia.CMbackend.services.EventoPartitaService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoPartitaServiceImpl implements EventoPartitaService {

    private final EventoPartitaRepository eventoPartitaRepository;

    @Override
    public List<EventoPartitaRespDTO> getEventiByPartitaId(UUID partitaId) {
        return eventoPartitaRepository.findByPartitaId(partitaId).stream()
                .map(this::toDTO)
                .toList();
    }

    // =================== MAPPER ===================

    private EventoPartitaRespDTO toDTO(EventoPartita evento) {
        UUID giocatoreSecondarioId = null;
        String nomeGiocatoreSecondario = null;

        if (evento.getGiocatoreSecondario() != null) {
            giocatoreSecondarioId = evento.getGiocatoreSecondario().getId();
            nomeGiocatoreSecondario = evento.getGiocatoreSecondario().getNome() + " " + evento.getGiocatoreSecondario().getCognome();
        }

        return new EventoPartitaRespDTO(
                evento.getId(),
                evento.getMinuto(),
                evento.getTipoEvento(),
                evento.getEsito(),
                evento.getNote(),
                evento.getPartita().getId(),
                evento.getGiocatorePrincipale().getId(),
                evento.getGiocatorePrincipale().getNome() + " " + evento.getGiocatorePrincipale().getCognome(),
                giocatoreSecondarioId,
                nomeGiocatoreSecondario,
                evento.getSquadra().getId()
        );
    }
}
