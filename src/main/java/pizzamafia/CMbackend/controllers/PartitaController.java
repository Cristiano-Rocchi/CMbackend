package pizzamafia.CMbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.entities.Partita;
import pizzamafia.CMbackend.services.PartitaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/partite")
@RequiredArgsConstructor
public class PartitaController {

    private final PartitaService partitaService;

    // =================== CREATE ===================

    @PostMapping
    public ResponseEntity<Partita> createPartita(@RequestBody Partita partita) {
        return ResponseEntity.ok(partitaService.save(partita));
    }

    // =================== GET ALL ===================

    @GetMapping
    public ResponseEntity<List<Partita>> getAllPartite() {
        return ResponseEntity.ok(partitaService.findAll());
    }

    // =================== GET BY ID ===================

    @GetMapping("/{id}")
    public ResponseEntity<Partita> getPartitaById(@PathVariable UUID id) {
        return partitaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =================== DELETE ===================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartita(@PathVariable UUID id) {
        partitaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
