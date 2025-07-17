package pizzamafia.CMbackend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Modulo;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.helpers.SelezioneCpuHelper;
import pizzamafia.CMbackend.payloads.partita.*;
import pizzamafia.CMbackend.repositories.FormazioneRepository;
import pizzamafia.CMbackend.repositories.GiocatoreRepository;
import pizzamafia.CMbackend.repositories.PartitaRepository;
import pizzamafia.CMbackend.repositories.SquadraRepository;
import pizzamafia.CMbackend.services.FormazioneService;
import pizzamafia.CMbackend.helpers.ValutazioneGiocatoreHelper;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormazioneServiceImpl implements FormazioneService {

    private final FormazioneRepository formazioneRepository;
    private final PartitaRepository partitaRepository;
    private final SquadraRepository squadraRepository;
    private final GiocatoreRepository giocatoreRepository;

    // =================== CREATE ===================

    @Override
    public FormazioneRespDTO create(NewFormazioneDTO dto) {
        Partita partita = partitaRepository.findById(dto.partitaId())
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));

        Squadra squadra = squadraRepository.findById(dto.squadraId())
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));

        Formazione formazione = Formazione.builder()
                .partita(partita)
                .squadra(squadra)
                .modulo(dto.modulo())
                .build();

        List<Titolari> titolari = dto.titolari().stream()
                .map(titolareDTO -> {
                    Giocatore g = giocatoreRepository.findById(titolareDTO.giocatoreId())
                            .orElseThrow(() -> new RuntimeException("Giocatore non trovato"));
                    return Titolari.builder()
                            .formazione(formazione)
                            .giocatore(g)
                            .ruolo(titolareDTO.ruolo())
                            .valoreEffettivo(ValutazioneGiocatoreHelper.calcolaValoreEffettivo(g, titolareDTO.ruolo()))
                            .build();
                })
                .toList();

        formazione.setTitolari(titolari);

        return toRespDTO(formazioneRepository.save(formazione));
    }

    // =================== FIND BY ID ===================

    @Override
    public FormazioneRespDTO findById(UUID id) {
        Formazione formazione = formazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formazione non trovata"));
        return toRespDTO(formazione);
    }

    // =================== FIND ALL ===================

    @Override
    public List<FormazioneRespDTO> findAll() {
        return formazioneRepository.findAll().stream()
                .map(this::toRespDTO)
                .toList();
    }

    // =================== DELETE ===================

    @Override
    public void deleteById(UUID id) {
        formazioneRepository.deleteById(id);
    }

    //TROVA SQUADRE
    public UUID trovaAltraSquadra(UUID idPartita, UUID idSquadraUtente) {
        Partita partita = partitaRepository.findById(idPartita)
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));

        UUID idCasa = partita.getSquadraCasa().getId();
        UUID idTrasferta = partita.getSquadraTrasferta().getId();

        if (idCasa.equals(idSquadraUtente)) return idTrasferta;
        if (idTrasferta.equals(idSquadraUtente)) return idCasa;

        throw new RuntimeException("La squadra indicata non partecipa a questa partita.");
    }

    //GENRA FORMAZIONI
    public void generaFormazioneAutomaticaCpu(UUID idPartita, UUID idSquadra) {
        Partita partita = partitaRepository.findById(idPartita)
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));

        Squadra squadra = squadraRepository.findById(idSquadra)
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));

        List<Giocatore> rosa = squadra.getGiocatori();

        // 1. Ottieni i titolari migliori per il modulo 4-4-2
        List<Titolari> titolari = SelezioneCpuHelper.generaTitolariDalModulo(rosa, "_4_4_2");

        // 2. Crea e salva la formazione
        Formazione formazione = Formazione.builder()
                .partita(partita)
                .squadra(squadra)
                .modulo(Modulo.valueOf("_4_4_2"))
                .titolari(titolari)
                .build();

        for (Titolari t : titolari) {
            t.setFormazione(formazione);
        }

        formazioneRepository.save(formazione);
    }


    // =================== MAPPING ===================

    private FormazioneRespDTO toRespDTO(Formazione f) {
        List<TitolareRespDTO> titolari = f.getTitolari().stream()
                .map(t -> new TitolareRespDTO(
                        t.getGiocatore().getId(),
                        t.getGiocatore().getNome(),
                        t.getGiocatore().getCognome(),
                        t.getRuolo(),
                        t.getValoreEffettivo()
                ))
                .toList();

        return new FormazioneRespDTO(
                f.getId(),
                f.getPartita().getId(),
                f.getSquadra().getId(),
                f.getModulo(),
                titolari
        );
    }


}

