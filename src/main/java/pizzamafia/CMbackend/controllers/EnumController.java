package pizzamafia.CMbackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pizzamafia.CMbackend.enums.Piede;
import pizzamafia.CMbackend.enums.Ruolo;

@RestController
@RequestMapping("/enums")
public class EnumController {

    // ========== ENUM PIEDI ==========
    @GetMapping("/piedi")
    public Piede[] getPiedi() {
        return Piede.values();
    }

    // ========== ENUM RUOLI ==========
    @GetMapping("/ruoli")
    public Ruolo[] getRuoli() {
        return Ruolo.values();
    }
}

