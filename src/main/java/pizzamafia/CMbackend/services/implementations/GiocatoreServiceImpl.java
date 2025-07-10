package pizzamafia.CMbackend.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.exceptions.NotFoundException;
import pizzamafia.CMbackend.payloads.giocatore.GiocatoreRespDTO;
import pizzamafia.CMbackend.payloads.giocatore.NewGiocatoreDTO;
import pizzamafia.CMbackend.payloads.giocatore.NewStatisticheTecnicheGiocatoreDTO;
import pizzamafia.CMbackend.payloads.giocatore.StatisticheTecnicheGiocatoreRespDTO;
import pizzamafia.CMbackend.repositories.GiocatoreRepository;
import pizzamafia.CMbackend.repositories.SquadraRepository;
import pizzamafia.CMbackend.services.GiocatoreService;

import java.util.List;
import java.util.UUID;

@Service
public class GiocatoreServiceImpl implements GiocatoreService {

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    @Autowired
    private SquadraRepository squadraRepository;

    // =================== CREATE ===================
    @Override
    public GiocatoreRespDTO create(NewGiocatoreDTO dto) {
        Squadra squadra = squadraRepository.findById(dto.squadraId())
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + dto.squadraId() + " non trovata."));

        // Costruisco l'entità Giocatore senza valore tecnico ancora calcolato
        Giocatore giocatore = Giocatore.builder()
                .nome(dto.nome())
                .cognome(dto.cognome())
                .dataDiNascita(dto.dataDiNascita())
                .nazionalita(dto.nazionalita())
                .altezza(dto.altezza())
                .piede(dto.piede())
                .ruolo(dto.ruolo())
                .squadra(squadra)
                .valoreTecnico(0) // placeholder, lo calcoliamo dopo
                .build();

        // Creo l'entità StatisticheTecnicheGiocatore e la collego al giocatore
        NewStatisticheTecnicheGiocatoreDTO s = dto.statisticheTecniche();
        StatisticheTecnicheGiocatore stats = StatisticheTecnicheGiocatore.builder()
                .attacco(s.attacco())
                .difesa(s.difesa())
                .velocita(s.velocita())
                .tiro(s.tiro())
                .passaggio(s.passaggio())
                .portiere(s.portiere())
                .giocatore(giocatore)
                .build();

        giocatore.setStatistiche(stats);

        // =================== CALCOLO VALORE TECNICO ===================
        // Usa la tabella dei pesi per calcolare il valore finale in base al ruolo
        double valore = calcolaValoreTecnico(dto.ruolo(), stats);
        giocatore.setValoreTecnico((int) Math.round(valore));

        // =================== SALVATAGGIO ===================
        giocatoreRepository.save(giocatore);

        return new GiocatoreRespDTO(
                giocatore.getId(),
                giocatore.getNome(),
                giocatore.getCognome(),
                giocatore.getDataDiNascita(),
                giocatore.getNazionalita(),
                giocatore.getAltezza(),
                giocatore.getPiede(),
                giocatore.getRuolo(),
                giocatore.getValoreTecnico(),
                squadra.getId(),
                new StatisticheTecnicheGiocatoreRespDTO(
                        stats.getAttacco(),
                        stats.getDifesa(),
                        stats.getVelocita(),
                        stats.getTiro(),
                        stats.getPassaggio(),
                        stats.getPortiere()
                )
        );

    }


    // =================== CALCOLO VALORE TECNICO ===================
    private double calcolaValoreTecnico(Ruolo ruolo, StatisticheTecnicheGiocatore s) {
        return switch (ruolo) {
            case PORTIERE -> s.getPortiere() * 0.6 + s.getVelocita() * 0.2 + s.getDifesa() * 0.1 + s.getPassaggio() * 0.1;
            case DIFENSORE_CENTRALE -> s.getDifesa() * 0.5 + s.getVelocita() * 0.2 + s.getPassaggio() * 0.2 + s.getAttacco() * 0.1;
            case TERZINO -> s.getDifesa() * 0.3 + s.getVelocita() * 0.3 + s.getPassaggio() * 0.2 + s.getTiro() * 0.1 + s.getAttacco() * 0.1;
            case CENTROCAMPISTA_DIFENSIVO -> s.getDifesa() * 0.3 + s.getAttacco() * 0.2 + s.getPassaggio() * 0.2 + s.getVelocita() * 0.2 + s.getTiro() * 0.1;
            case CENTROCAMPISTA_OFFENSIVO -> s.getAttacco() * 0.3 + s.getTiro() * 0.2 + s.getVelocita() * 0.2 + s.getPassaggio() * 0.3;
            case ALA -> s.getAttacco() * 0.3 + s.getVelocita() * 0.3 + s.getTiro() * 0.2 + s.getPassaggio() * 0.1 + s.getDifesa() * 0.1;
            case ATTACCANTE_ESTERNO -> s.getAttacco() * 0.4 + s.getVelocita() * 0.3 + s.getTiro() * 0.2 + s.getPassaggio() * 0.1;
            case SECONDA_PUNTA -> s.getAttacco() * 0.4 + s.getTiro() * 0.3 + s.getVelocita() * 0.2 + s.getPassaggio() * 0.2;
            case BOMBER -> s.getAttacco() * 0.5 + s.getTiro() * 0.3 + s.getVelocita() * 0.2;
        };
    }

    // =================== GET BY ID ===================
    @Override
    public GiocatoreRespDTO getById(UUID id) {
        Giocatore giocatore = giocatoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giocatore con ID " + id + " non trovato."));
        return mapToDTO(giocatore);
    }

    // =================== GET ALL ===================
    @Override
    public List<GiocatoreRespDTO> getAll() {
        return giocatoreRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =================== UPDATE ===================
    @Override
    public GiocatoreRespDTO update(UUID id, NewGiocatoreDTO dto) {
        Giocatore giocatore = giocatoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giocatore con ID " + id + " non trovato."));

        Squadra squadra = squadraRepository.findById(dto.squadraId())
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + dto.squadraId() + " non trovata."));

        // Aggiorna i campi del giocatore
        giocatore.setNome(dto.nome());
        giocatore.setCognome(dto.cognome());
        giocatore.setDataDiNascita(dto.dataDiNascita());
        giocatore.setNazionalita(dto.nazionalita());
        giocatore.setAltezza(dto.altezza());
        giocatore.setPiede(dto.piede());
        giocatore.setRuolo(dto.ruolo());
        giocatore.setSquadra(squadra);

        // Aggiorna le statistiche
        StatisticheTecnicheGiocatore s = giocatore.getStatistiche();
        NewStatisticheTecnicheGiocatoreDTO dtoStats = dto.statisticheTecniche();

        s.setAttacco(dtoStats.attacco());
        s.setDifesa(dtoStats.difesa());
        s.setVelocita(dtoStats.velocita());
        s.setTiro(dtoStats.tiro());
        s.setPassaggio(dtoStats.passaggio());
        s.setPortiere(dtoStats.portiere());

        // Ricalcola il valore tecnico
        double valore = calcolaValoreTecnico(dto.ruolo(), s);
        giocatore.setValoreTecnico((int) Math.round(valore));

        giocatoreRepository.save(giocatore);

        return mapToDTO(giocatore);
    }

    // =================== DELETE ===================
    @Override
    public void delete(UUID id) {
        Giocatore giocatore = giocatoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giocatore con ID " + id + " non trovato."));
        giocatoreRepository.delete(giocatore);
    }

    // =================== MAPPING ===================
    private GiocatoreRespDTO mapToDTO(Giocatore g) {
        StatisticheTecnicheGiocatore stats = g.getStatistiche();

        StatisticheTecnicheGiocatoreRespDTO statsDTO = new StatisticheTecnicheGiocatoreRespDTO(
                stats.getAttacco(),
                stats.getDifesa(),
                stats.getVelocita(),
                stats.getTiro(),
                stats.getPassaggio(),
                stats.getPortiere()
        );

        return new GiocatoreRespDTO(
                g.getId(),
                g.getNome(),
                g.getCognome(),
                g.getDataDiNascita(),
                g.getNazionalita(),
                g.getAltezza(),
                g.getPiede(),
                g.getRuolo(),
                g.getValoreTecnico(),
                g.getSquadra().getId(),
                statsDTO
        );
    }


}
