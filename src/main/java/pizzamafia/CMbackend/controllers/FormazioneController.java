package pizzamafia.CMbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.payloads.partita.FormazioneRespDTO;
import pizzamafia.CMbackend.payloads.partita.NewFormazioneDTO;
import pizzamafia.CMbackend.services.FormazioneService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/formazioni")
@RequiredArgsConstructor
public class FormazioneController {

    private final FormazioneService formazioneService;

    // =================== CREATE ===================

    @PostMapping
    public ResponseEntity<FormazioneRespDTO> createFormazione(@RequestBody NewFormazioneDTO dto) {
        return ResponseEntity.ok(formazioneService.create(dto));
    }

    // =================== GET BY ID ===================

    @GetMapping("/{id}")
    public ResponseEntity<FormazioneRespDTO> getFormazioneById(@PathVariable UUID id) {
        return ResponseEntity.ok(formazioneService.findById(id));
    }

    // =================== GET ALL ===================

    @GetMapping
    public ResponseEntity<List<FormazioneRespDTO>> getAllFormazioni() {
        return ResponseEntity.ok(formazioneService.findAll());
    }

    // =================== DELETE ===================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormazione(@PathVariable UUID id) {
        formazioneService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
