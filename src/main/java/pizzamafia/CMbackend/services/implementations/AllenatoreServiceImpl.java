package pizzamafia.CMbackend.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.Allenatore;
import pizzamafia.CMbackend.entities.Squadra;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.payloads.allenatore.AllenatoreRespDTO;
import pizzamafia.CMbackend.payloads.allenatore.NewAllenatoreDTO;
import pizzamafia.CMbackend.repositories.AllenatoreRepository;
import pizzamafia.CMbackend.repositories.SquadraRepository;
import pizzamafia.CMbackend.services.AllenatoreService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AllenatoreServiceImpl implements AllenatoreService {

    private final AllenatoreRepository allenatoreRepository;
    private final SquadraRepository squadraRepository;

    @Override
    @Transactional
    public AllenatoreRespDTO create(NewAllenatoreDTO dto) {
        Squadra squadra = squadraRepository.findById(dto.squadraId())
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + dto.squadraId() + " non trovata."));

        if (allenatoreRepository.existsBySquadra_Id(squadra.getId())) {
            throw new IllegalStateException("La squadra ha già un allenatore assegnato.");
        }

        Allenatore a = Allenatore.builder()
                .nome(dto.nome())
                .cognome(dto.cognome())
                .ideaDiGioco(dto.ideaDiGioco())
                .build();

        // set relazione bidirezionale
        a.setSquadra(squadra);
        squadra.setAllenatore(a);

        a = allenatoreRepository.save(a);
        // non è obbligatorio salvare squadra qui, ma non guasta:
        squadraRepository.save(squadra);

        return toDTO(a);
    }

    @Override
    public AllenatoreRespDTO getById(UUID id) {
        Allenatore a = allenatoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Allenatore con ID " + id + " non trovato."));
        return toDTO(a);
    }

    @Override
    public List<AllenatoreRespDTO> getAll() {
        return allenatoreRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public AllenatoreRespDTO update(UUID id, NewAllenatoreDTO dto) {
        Allenatore a = allenatoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Allenatore con ID " + id + " non trovato."));

        Squadra nuovaSquadra = squadraRepository.findById(dto.squadraId())
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + dto.squadraId() + " non trovata."));

        // se cambio squadra, stacco dal vecchio lato
        if (a.getSquadra() != null && !a.getSquadra().getId().equals(nuovaSquadra.getId())) {
            a.getSquadra().setAllenatore(null);
        }

        a.setNome(dto.nome());
        a.setCognome(dto.cognome());
        a.setIdeaDiGioco(dto.ideaDiGioco());
        a.setSquadra(nuovaSquadra);
        nuovaSquadra.setAllenatore(a);

        a = allenatoreRepository.save(a);
        squadraRepository.save(nuovaSquadra);

        return toDTO(a);
    }

    @Override
    public void delete(UUID id) {
        Allenatore a = allenatoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Allenatore con ID " + id + " non trovato."));
        // pulizia lato squadra
        if (a.getSquadra() != null) {
            a.getSquadra().setAllenatore(null);
            squadraRepository.save(a.getSquadra());
        }
        allenatoreRepository.delete(a);
    }

    @Override
    public AllenatoreRespDTO getBySquadraId(UUID squadraId) {
        Allenatore a = allenatoreRepository.findBySquadra_Id(squadraId)
                .orElseThrow(() -> new NotFoundException("Allenatore per squadra " + squadraId + " non trovato."));
        return toDTO(a);
    }

    private AllenatoreRespDTO toDTO(Allenatore a) {
        return new AllenatoreRespDTO(
                a.getNome(),
                a.getCognome(),
                a.getIdeaDiGioco(),
                a.getSquadra() != null ? a.getSquadra().getId() : null
        );
    }
}
