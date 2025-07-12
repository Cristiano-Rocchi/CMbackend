package pizzamafia.CMbackend.services.implementations;

import pizzamafia.CMbackend.entities.Squadra;
import pizzamafia.CMbackend.entities.Stadio;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.payloads.squadra.NewSquadraDTO;
import pizzamafia.CMbackend.payloads.squadra.SquadraRespDTO;
import pizzamafia.CMbackend.repositories.SquadraRepository;
import pizzamafia.CMbackend.repositories.StadioRepository;
import pizzamafia.CMbackend.services.SquadraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SquadraServiceImpl implements SquadraService {

    @Autowired
    private SquadraRepository squadraRepository;

    @Autowired
    private StadioRepository stadioRepository;

    // =================== CREATE ===================
    @Override
    public SquadraRespDTO create(NewSquadraDTO dto) {
        Stadio stadio = stadioRepository.findById(dto.stadioId())
                .orElseThrow(() -> new NotFoundException("Stadio con ID " + dto.stadioId() + " non trovato."));

        Squadra squadra = Squadra.builder()
                .nome(dto.nome())
                .coloriSociali(dto.coloriSociali())
                .magliaColorePrimario(dto.magliaColorePrimario())
                .magliaColoreSecondario(dto.magliaColoreSecondario())
                .stadio(stadio)
                .valoreTecnicoTotale(0) // inizialmente 0
                .build();

        squadraRepository.save(squadra);
        return mapToDTO(squadra);
    }

    // =================== GET BY ID ===================
    @Override
    public SquadraRespDTO getById(UUID id) {
        Squadra squadra = squadraRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + id + " non trovata."));
        return mapToDTO(squadra);
    }

    // =================== GET ALL ===================
    @Override
    public List<SquadraRespDTO> getAll() {
        return squadraRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =================== UPDATE ===================
    @Override
    public SquadraRespDTO update(UUID id, NewSquadraDTO dto) {
        Squadra squadra = squadraRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + id + " non trovata."));

        Stadio stadio = stadioRepository.findById(dto.stadioId())
                .orElseThrow(() -> new NotFoundException("Stadio con ID " + dto.stadioId() + " non trovato."));

        squadra.setNome(dto.nome());
        squadra.setColoriSociali(dto.coloriSociali());
        squadra.setMagliaColorePrimario(dto.magliaColorePrimario());
        squadra.setMagliaColoreSecondario(dto.magliaColoreSecondario());
        squadra.setStadio(stadio);

        squadraRepository.save(squadra);
        return mapToDTO(squadra);
    }

    // =================== DELETE ===================
    @Override
    public void delete(UUID id) {
        Squadra squadra = squadraRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + id + " non trovata."));
        squadraRepository.delete(squadra);
    }

    

    // =================== MAPPING ===================
    private SquadraRespDTO mapToDTO(Squadra s) {
        return new SquadraRespDTO(
                s.getId(),
                s.getNome(),
                s.getColoriSociali(),
                s.getMagliaColorePrimario(),
                s.getMagliaColoreSecondario(),
                s.getStadio().getNome(),
                s.getValoreTecnicoTotale()
        );
    }
}
