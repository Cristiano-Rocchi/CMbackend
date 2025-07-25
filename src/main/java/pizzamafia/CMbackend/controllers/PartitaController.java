package pizzamafia.CMbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.entities.Formazione;
import pizzamafia.CMbackend.entities.Partita;
import pizzamafia.CMbackend.payloads.partita.MarcatoreRespDTO;
import pizzamafia.CMbackend.payloads.partita.PartitaRespDTO;
import pizzamafia.CMbackend.payloads.partita.TitolareRespDTO;
import pizzamafia.CMbackend.repositories.FormazioneRepository;
import pizzamafia.CMbackend.repositories.TitolariRepository;
import pizzamafia.CMbackend.services.PartitaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/partite")
@RequiredArgsConstructor
public class PartitaController {

    private final PartitaService partitaService;

    private final TitolariRepository titolariRepository;

    private final FormazioneRepository formazioneRepository;

    // =================== CREATE ===================

    @PostMapping
    public ResponseEntity<Partita> createPartita(@RequestBody Partita partita) {
        return ResponseEntity.ok(partitaService.save(partita));
    }

    // =================== GET ALL ===================

    @GetMapping
    public ResponseEntity<List<PartitaRespDTO>> getAllPartite() {
        List<PartitaRespDTO> response = partitaService.findAll().stream()
                .map(this::toRespDTO)
                .toList();
        return ResponseEntity.ok(response);
    }


    // =================== GET BY ID ===================

    @GetMapping("/{id}")
    public ResponseEntity<PartitaRespDTO> getPartitaById(@PathVariable UUID id) {
        return partitaService.findById(id)
                .map(this::toRespDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // =================== DELETE ===================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartita(@PathVariable UUID id) {
        partitaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // =================== SIMULA PARTITA ===================

    @PostMapping("/{id}/simula")
    public ResponseEntity<PartitaRespDTO> simulaPartita(@PathVariable UUID id) {
        Partita partitaSimulata = partitaService.simulaPartita(id);
        return ResponseEntity.ok(toRespDTO(partitaSimulata));
    }

    private PartitaRespDTO toRespDTO(Partita p) {
        List<MarcatoreRespDTO> marcatori = p.getMarcatori().stream()
                .map(m -> new MarcatoreRespDTO(
                        m.getGiocatore().getId(),
                        m.getGiocatore().getNome(),
                        m.getGiocatore().getCognome(),
                        m.getSquadra().getNome(),
                        m.getMinuto()
                ))
                .toList();

        // Recupera le due formazioni
        List<Formazione> formazioni = formazioneRepository.findByPartitaId(p.getId());

        UUID idCasa = p.getSquadraCasa().getId();
        UUID idTrasferta = p.getSquadraTrasferta().getId();

        Formazione formazioneCasa = formazioni.stream()
                .filter(f -> f.getSquadra().getId().equals(idCasa))
                .findFirst()
                .orElse(null);

        Formazione formazioneTrasferta = formazioni.stream()
                .filter(f -> f.getSquadra().getId().equals(idTrasferta))
                .findFirst()
                .orElse(null);

        List<TitolareRespDTO> titolariCasa = formazioneCasa != null
                ? titolariRepository.findByFormazioneId(formazioneCasa.getId()).stream()
                .map(t -> new TitolareRespDTO(
                        t.getGiocatore().getId(),
                        t.getGiocatore().getNome(),
                        t.getGiocatore().getCognome(),
                        t.getRuolo(),
                        t.getValoreEffettivo()
                ))
                .toList()
                : List.of();

        List<TitolareRespDTO> titolariTrasferta = formazioneTrasferta != null
                ? titolariRepository.findByFormazioneId(formazioneTrasferta.getId()).stream()
                .map(t -> new TitolareRespDTO(
                        t.getGiocatore().getId(),
                        t.getGiocatore().getNome(),
                        t.getGiocatore().getCognome(),
                        t.getRuolo(),
                        t.getValoreEffettivo()
                ))
                .toList()
                : List.of();

        return new PartitaRespDTO(
                p.getId(),
                p.getSquadraCasa().getNome(),
                p.getSquadraTrasferta().getNome(),
                p.getGoalCasa(),
                p.getGoalTrasferta(),
                p.getDataOra(),
                p.getCompetizione().toString(),
                marcatori,
                titolariCasa,
                titolariTrasferta
        );
    }

}




