package pizzamafia.CMbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.payloads.partita.TitolareRespDTO;
import pizzamafia.CMbackend.services.TitolariService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/titolari")
@RequiredArgsConstructor
public class TitolariController {

    private final TitolariService titolariService;

    // =================== GET BY FORMAZIONE ===================

    @GetMapping("/formazione/{formazioneId}")
    public ResponseEntity<List<TitolareRespDTO>> getByFormazioneId(@PathVariable UUID formazioneId) {
        return ResponseEntity.ok(titolariService.getByFormazioneId(formazioneId));
    }

    // =================== DELETE BY ID ===================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        titolariService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
