package pizzamafia.CMbackend.controllers;

import pizzamafia.CMbackend.payloads.giocatore.NewGiocatoreDTO;
import pizzamafia.CMbackend.payloads.giocatore.GiocatoreRespDTO;
import pizzamafia.CMbackend.services.GiocatoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/giocatori")
public class GiocatoreController {

    @Autowired
    private GiocatoreService giocatoreService;

    // =================== CREATE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GiocatoreRespDTO> create(@RequestBody @Valid NewGiocatoreDTO body) {
        return ResponseEntity.ok(giocatoreService.create(body));
    }

    // =================== GET BY ID ===================
    @GetMapping("/{id}")
    public ResponseEntity<GiocatoreRespDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(giocatoreService.getById(id));
    }

    // =================== GET ALL ===================
    @GetMapping
    public ResponseEntity<List<GiocatoreRespDTO>> getAll() {
        return ResponseEntity.ok(giocatoreService.getAll());
    }

    // =================== UPDATE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GiocatoreRespDTO> update(@PathVariable UUID id, @RequestBody @Valid NewGiocatoreDTO body) {
        return ResponseEntity.ok(giocatoreService.update(id, body));
    }

    // =================== DELETE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        giocatoreService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/squadra/{id}")
    public List<GiocatoreRespDTO> getGiocatoriBySquadra(@PathVariable UUID id) {
        return giocatoreService.findAllBySquadraId(id);
    }

}
