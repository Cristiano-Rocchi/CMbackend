package pizzamafia.CMbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.payloads.allenatore.AllenatoreRespDTO;
import pizzamafia.CMbackend.payloads.allenatore.NewAllenatoreDTO;
import pizzamafia.CMbackend.services.AllenatoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/allenatori")
@RequiredArgsConstructor
public class AllenatoreController {

    private final AllenatoreService allenatoreService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AllenatoreRespDTO create(@RequestBody NewAllenatoreDTO dto) {
        return allenatoreService.create(dto);
    }

    @GetMapping("/{id}")
    public AllenatoreRespDTO getById(@PathVariable UUID id) {
        return allenatoreService.getById(id);
    }

    @GetMapping
    public List<AllenatoreRespDTO> getAll() {
        return allenatoreService.getAll();
    }

    @PutMapping("/{id}")
    public AllenatoreRespDTO update(@PathVariable UUID id, @RequestBody NewAllenatoreDTO dto) {
        return allenatoreService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        allenatoreService.delete(id);
    }

    @GetMapping("/by-squadra/{squadraId}")
    public AllenatoreRespDTO getBySquadra(@PathVariable UUID squadraId) {
        return allenatoreService.getBySquadraId(squadraId);
    }
}
