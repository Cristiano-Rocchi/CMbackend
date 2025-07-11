package pizzamafia.CMbackend.controllers;

import pizzamafia.CMbackend.payloads.squadra.NewSquadraDTO;
import pizzamafia.CMbackend.payloads.squadra.SquadraRespDTO;
import pizzamafia.CMbackend.repositories.SquadraRepository;
import pizzamafia.CMbackend.services.SquadraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/squadre")
public class SquadraController {

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private SquadraRepository squadraRepository;

    // =================== CREATE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SquadraRespDTO> create(@RequestBody @Valid NewSquadraDTO body) {
        return ResponseEntity.ok(squadraService.create(body));
    }

    // =================== GET BY ID ===================
    @GetMapping("/{id}")
    public ResponseEntity<SquadraRespDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(squadraService.getById(id));
    }

    // =================== GET ALL ===================
    @GetMapping
    public ResponseEntity<List<SquadraRespDTO>> getAll() {
        return ResponseEntity.ok(squadraService.getAll());
    }

    // =================== UPDATE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SquadraRespDTO> update(@PathVariable UUID id, @RequestBody @Valid NewSquadraDTO body) {
        return ResponseEntity.ok(squadraService.update(id, body));
    }

    // =================== DELETE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        squadraService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ===================CERCA PER NOME ===================
    @GetMapping("/search")
    public List<SquadraRespDTO> searchSquadre(@RequestParam String q) {
        return squadraRepository.findByNomeContainingIgnoreCase(q).stream()
                .map(s -> new SquadraRespDTO(
                        s.getId(),
                        s.getNome(),
                        s.getColoriSociali(),
                        s.getMagliaColorePrimario(),
                        s.getMagliaColoreSecondario(),
                        s.getStadio() != null ? s.getStadio().getId() : null,
                        s.getValoreTecnicoTotale()
                ))
                .toList();
    }

}
