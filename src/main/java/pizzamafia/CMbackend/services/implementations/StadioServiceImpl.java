package pizzamafia.CMbackend.services.implementations;

import pizzamafia.CMbackend.entities.Stadio;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.payloads.stadio.NewStadioDTO;
import pizzamafia.CMbackend.payloads.stadio.StadioRespDTO;
import pizzamafia.CMbackend.repositories.StadioRepository;
import pizzamafia.CMbackend.services.StadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StadioServiceImpl implements StadioService {

    @Autowired
    private StadioRepository stadioRepository;

    // =================== CREATE ===================
    @Override
    public StadioRespDTO create(NewStadioDTO dto) {
        Stadio stadio = Stadio.builder()
                .nome(dto.nome())
                .luogo(dto.luogo())
                .capienza(dto.capienza())
                .build();

        stadioRepository.save(stadio);
        return mapToDTO(stadio);
    }

    // =================== READ (getById) ===================
    @Override
    public StadioRespDTO getById(UUID id) {
        Stadio stadio = stadioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stadio con ID " + id + " non trovato."));
        return mapToDTO(stadio);
    }

    // =================== READ (getAll) ===================
    @Override
    public List<StadioRespDTO> getAll() {
        return stadioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =================== UPDATE ===================
    @Override
    public StadioRespDTO update(UUID id, NewStadioDTO dto) {
        Stadio stadio = stadioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stadio con ID " + id + " non trovato."));

        stadio.setNome(dto.nome());
        stadio.setLuogo(dto.luogo());
        stadio.setCapienza(dto.capienza());

        stadioRepository.save(stadio);
        return mapToDTO(stadio);
    }

    // =================== DELETE ===================
    @Override
    public void delete(UUID id) {
        Stadio stadio = stadioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stadio con ID " + id + " non trovato."));
        stadioRepository.delete(stadio);
    }

    // =================== MAPPING ===================
    private StadioRespDTO mapToDTO(Stadio s) {
        return new StadioRespDTO(s.getId(), s.getNome(), s.getLuogo(), s.getCapienza());
    }
}
