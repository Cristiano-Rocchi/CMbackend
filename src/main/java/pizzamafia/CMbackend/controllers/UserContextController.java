package pizzamafia.CMbackend.controllers;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.helpers.UserTeamContext;
import pizzamafia.CMbackend.payloads.partita.SceltaSquadraDTO;

import java.util.UUID;

@RestController
@RequestMapping("/utente")
public class UserContextController {

    @Autowired
    private UserTeamContext userTeamContext;

    // Imposta la squadra utente selezionata dal frontend
    @PostMapping("/squadra")
    public ResponseEntity<Void> setUserTeam(@RequestBody SceltaSquadraDTO body) {
        userTeamContext.setUserTeamId(body.idSquadra());
        return ResponseEntity.ok().build();
    }


    // Ritorna la squadra attualmente selezionata (opzionale ma utile)
    @GetMapping("/squadra")
    public ResponseEntity<UUID> getUserTeam() {
        if (!userTeamContext.isInitialized()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userTeamContext.getUserTeamId());
    }
}
