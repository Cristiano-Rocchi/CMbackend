package pizzamafia.CMbackend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.Partita;
import pizzamafia.CMbackend.repositories.PartitaRepository;
import pizzamafia.CMbackend.services.PartitaService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartitaServiceImpl implements PartitaService {

    private final PartitaRepository partitaRepository;

    private final pizzamafia.CMbackend.services.SimulazionePartitaService simulazionePartitaService;


    // =================== SAVE ===================

    @Override
    public Partita save(Partita partita) {
        return partitaRepository.save(partita);
    }

    // =================== FIND BY ID ===================

    @Override
    public Optional<Partita> findById(UUID id) {
        return partitaRepository.findById(id);
    }

    // =================== FIND ALL ===================

    @Override
    public List<Partita> findAll() {
        return partitaRepository.findAll();
    }

    // =================== DELETE BY ID ===================

    @Override
    public void deleteById(UUID id) {
        partitaRepository.deleteById(id);
    }


    // =================== SIMULA PARTITA ===================

    @Override
    public Partita simulaPartita(UUID id) {
        return simulazionePartitaService.simula(id);
    }

}
