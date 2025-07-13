package pizzamafia.CMbackend.services.implementations;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.payloads.partita.TitolareRespDTO;
import pizzamafia.CMbackend.repositories.TitolariRepository;
import pizzamafia.CMbackend.services.TitolariService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TitolariServiceImpl implements TitolariService {

    private final TitolariRepository titolariRepository;

    // =================== GET BY FORMAZIONE ===================

    @Override
    public List<TitolareRespDTO> getByFormazioneId(UUID formazioneId) {
        return titolariRepository.findAll().stream()
                .filter(t -> t.getFormazione().getId().equals(formazioneId))
                .map(t -> new TitolareRespDTO(
                        t.getGiocatore().getId(),
                        t.getGiocatore().getNome(),
                        t.getGiocatore().getCognome(),
                        t.getRuolo(),
                        t.getValoreEffettivo()
                ))
                .toList();
    }

    // =================== DELETE BY ID ===================

    @Override
    public void deleteById(UUID id) {
        titolariRepository.deleteById(id);
    }
}

