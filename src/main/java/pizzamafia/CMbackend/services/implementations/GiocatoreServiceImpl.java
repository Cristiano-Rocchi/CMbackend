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
import pizzamafia.CMbackend.helpers.ValutazioneGiocatoreHelper;
import pizzamafia.CMbackend.helpers.ValutazioneSquadraHelper;


import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        double valore = ValutazioneGiocatoreHelper.calcolaValoreTecnico(dto.ruolo(), stats);

        giocatore.setValoreTecnico((int) Math.round(valore));

        // =================== SALVATAGGIO ===================
        giocatoreRepository.save(giocatore);
        aggiornaValoreTecnicoSquadra(squadra);

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
                squadra.getNome(),
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




    // =================== AGGIORNA VALORE SQUADRA ===================
    private void aggiornaValoreTecnicoSquadra(Squadra squadra) {
        squadra = squadraRepository.findById(squadra.getId())
                .orElseThrow(() -> new NotFoundException("Squadra non trovata"));

        List<Giocatore> rosa = squadra.getGiocatori();

        int valoreTotale = ValutazioneSquadraHelper.calcolaValoreSquadra(rosa);

        squadra.setValoreTecnicoTotale(valoreTotale);
        squadraRepository.save(squadra);
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

        // Salva la squadra prima della modifica (serve in caso cambi)
        Squadra squadraPrecedente = giocatore.getSquadra();

        // Carica la nuova squadra richiesta
        Squadra nuovaSquadra = squadraRepository.findById(dto.squadraId())
                .orElseThrow(() -> new NotFoundException("Squadra con ID " + dto.squadraId() + " non trovata."));

        // Aggiorna i dati del giocatore
        giocatore.setNome(dto.nome());
        giocatore.setCognome(dto.cognome());
        giocatore.setDataDiNascita(dto.dataDiNascita());
        giocatore.setNazionalita(dto.nazionalita());
        giocatore.setAltezza(dto.altezza());
        giocatore.setPiede(dto.piede());
        giocatore.setRuolo(dto.ruolo());
        giocatore.setSquadra(nuovaSquadra);

        // Aggiorna le statistiche
        StatisticheTecnicheGiocatore s = giocatore.getStatistiche();
        NewStatisticheTecnicheGiocatoreDTO dtoStats = dto.statisticheTecniche();

        s.setAttacco(dtoStats.attacco());
        s.setDifesa(dtoStats.difesa());
        s.setVelocita(dtoStats.velocita());
        s.setTiro(dtoStats.tiro());
        s.setPassaggio(dtoStats.passaggio());
        s.setPortiere(dtoStats.portiere());

        // Ricalcola il valore tecnico in base al nuovo ruolo + statistiche
        double valore = ValutazioneGiocatoreHelper.calcolaValoreTecnico(dto.ruolo(), s);

        giocatore.setValoreTecnico((int) Math.round(valore));

        // Salva il giocatore aggiornato
        giocatoreRepository.save(giocatore);

        // Aggiorna il valore tecnico della nuova squadra
        aggiornaValoreTecnicoSquadra(nuovaSquadra);

        // Se è cambiata la squadra, aggiorna anche quella precedente
        if (!squadraPrecedente.getId().equals(nuovaSquadra.getId())) {
            aggiornaValoreTecnicoSquadra(squadraPrecedente);
        }

        return mapToDTO(giocatore);
    }


    // =================== GET BY SQUADRA ===================
    @Override
    public List<GiocatoreRespDTO> findAllBySquadraId(UUID id) {
        List<Giocatore> giocatori = giocatoreRepository.findBySquadraId(id);
        return giocatori.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //======CERCA PER NOME O COGNOME
    @Override
    public List<GiocatoreRespDTO> searchByNomeOCognome(String query) {
        List<Giocatore> risultati = giocatoreRepository
                .findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(query, query);

        return risultati.stream()
                .map(this::mapToDTO)
                .toList();
    }




    // =================== DELETE ===================
    @Override
    public void delete(UUID id) {
        Giocatore giocatore = giocatoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giocatore con ID " + id + " non trovato."));

        Squadra squadra = giocatore.getSquadra(); // salva la squadra prima dell'eliminazione

        giocatoreRepository.delete(giocatore);

        // aggiorna il valore tecnico della squadra dopo la rimozione
        aggiornaValoreTecnicoSquadra(squadra);
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
                g.getSquadra().getNome(),
                statsDTO
        );
    }


}
