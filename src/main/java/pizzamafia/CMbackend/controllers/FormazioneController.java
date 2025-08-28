package pizzamafia.CMbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.helpers.UserTeamContext;
import pizzamafia.CMbackend.helpers.utility.ModuloUtils;
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

    @Autowired
    private UserTeamContext userTeamContext;

    // =================== CREATE ===================

    @PostMapping
    public ResponseEntity<Void> createFormazione(@RequestBody @Valid NewFormazioneDTO dto) {
        UUID squadraId = dto.squadraId();
        UUID partitaId = dto.partitaId();

        // Solo la squadra utente può impostare la formazione
        if (!userTeamContext.isUserTeam(squadraId)) {
            return ResponseEntity.badRequest().build();
        }

        // Validazione dei titolari rispetto al modulo selezionato
        ModuloUtils.validaTitolariPerModulo(dto.modulo(), dto.titolari());

        // Salva la formazione della squadra utente
        formazioneService.create(dto);

        // Recupera l'altra squadra e genera la formazione CPU
        UUID squadraCpuId = formazioneService.trovaAltraSquadra(partitaId, squadraId);
        formazioneService.generaFormazioneAutomaticaCpu(partitaId, squadraCpuId);

        return ResponseEntity.ok().build();
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
