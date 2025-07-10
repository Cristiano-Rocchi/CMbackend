package pizzamafia.CMbackend.controllers;

import pizzamafia.CMbackend.payloads.stadio.NewStadioDTO;
import pizzamafia.CMbackend.payloads.stadio.StadioRespDTO;
import pizzamafia.CMbackend.repositories.StadioRepository;
import pizzamafia.CMbackend.services.StadioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stadi")
public class StadioController {

    @Autowired
    private StadioService stadioService;

    @Autowired
    private StadioRepository stadioRepository;

    // =================== CREATE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StadioRespDTO> create(@RequestBody @Valid NewStadioDTO body) {
        StadioRespDTO result = stadioService.create(body);
        return ResponseEntity.ok(result);
    }

    // =================== GET BY ID ===================
    @GetMapping("/{id}")
    public ResponseEntity<StadioRespDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(stadioService.getById(id));
    }

    // =================== GET ALL ===================
    @GetMapping
    public ResponseEntity<List<StadioRespDTO>> getAll() {
        return ResponseEntity.ok(stadioService.getAll());
    }

    // =================== UPDATE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StadioRespDTO> update(@PathVariable UUID id, @RequestBody @Valid NewStadioDTO body) {
        return ResponseEntity.ok(stadioService.update(id, body));
    }

    // =================== DELETE ===================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        stadioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =================== SEARCH BY NOME ===================
    @GetMapping("/search")
    public List<StadioRespDTO> searchByNome(@RequestParam String q) {
        return stadioRepository.findByNomeContainingIgnoreCase(q).stream()
                .map(stadio -> new StadioRespDTO(
                        stadio.getId(),
                        stadio.getNome(),
                        stadio.getLuogo(),
                        stadio.getCapienza()
                ))
                .toList();
    }

}
