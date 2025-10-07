package pizzamafia.CMbackend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <--- IMPORT
import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.helpers.partita.SimulazionePartitaHelper;

import pizzamafia.CMbackend.repositories.*;
import pizzamafia.CMbackend.services.EventoPartitaService;
import pizzamafia.CMbackend.services.SimulazionePartitaService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimulazionePartitaServiceImpl implements SimulazionePartitaService {

    private final PartitaRepository partitaRepository;
    private final FormazioneRepository formazioneRepository;
    private final TitolariRepository titolariRepository;
    private final EventoPartitaRepository eventoPartitaRepository;
    private final EventoPartitaService eventoPartitaService;

    // =================== SIMULAZIONE PARTITA ===================
    @Transactional // <--- AGGIUNTO
    @Override
    public Partita simula(UUID partitaId) {
        // 1. Recupera la partita
        Partita partita = partitaRepository.findById(partitaId)
                .orElseThrow(() -> new NotFoundException("Partita non trovata"));

        // 2. Recupera le formazioni
        List<Formazione> formazioni = formazioneRepository.findByPartitaId(partitaId);
        if (formazioni.size() != 2) throw new IllegalStateException("Formazioni incomplete");

        Formazione formazioneCasa = formazioni.stream()
                .filter(f -> f.getSquadra().equals(partita.getSquadraCasa()))
                .findFirst().orElseThrow(() -> new NotFoundException("Formazione casa mancante"));

        Formazione formazioneTrasferta = formazioni.stream()
                .filter(f -> f.getSquadra().equals(partita.getSquadraTrasferta()))
                .findFirst().orElseThrow(() -> new NotFoundException("Formazione trasferta mancante"));

        // 3. Recupera i titolari
        List<Titolari> titolariCasa = titolariRepository.findByFormazioneId(formazioneCasa.getId());
        List<Titolari> titolariTrasferta = titolariRepository.findByFormazioneId(formazioneTrasferta.getId());

        // 4. Pulisci eventuali eventi precedenti della stessa partita  <--- AGGIUNTO
        eventoPartitaRepository.deleteByPartitaId(partitaId);

        // 5. Simula la partita
        List<EventoPartita> eventi = SimulazionePartitaHelper.simulaPartita(partita, titolariCasa, titolariTrasferta);

        // 6. Salva gli eventi
        eventoPartitaRepository.saveAll(eventi);

        // 7. Conta i gol per squadra
        UUID casaId = partita.getSquadraCasa().getId();
        UUID trasfertaId = partita.getSquadraTrasferta().getId();

        long golCasa = eventi.stream()
                .filter(e -> e.getTipoEvento() == TipoEvento.GOL)
                .filter(e -> "RETE".equals(e.getEsito()))
                .filter(e -> e.getSquadra() != null && e.getSquadra().getId() != null)
                .filter(e -> e.getSquadra().getId().equals(casaId))
                .count();

        long golTrasferta = eventi.stream()
                .filter(e -> e.getTipoEvento() == TipoEvento.GOL)
                .filter(e -> "RETE".equals(e.getEsito()))
                .filter(e -> e.getSquadra() != null && e.getSquadra().getId() != null)
                .filter(e -> e.getSquadra().getId().equals(trasfertaId))
                .count();

        // 8. Aggiorna il risultato nella partita
        partita.setGoalCasa((int) golCasa);
        partita.setGoalTrasferta((int) golTrasferta);
        partitaRepository.save(partita);

        return partita;
    }
}
