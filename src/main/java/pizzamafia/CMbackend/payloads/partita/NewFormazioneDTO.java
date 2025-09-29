package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.MentalitaTattica;
import pizzamafia.CMbackend.enums.Modulo;

import java.util.List;
import java.util.UUID;

public record NewFormazioneDTO(
        UUID partitaId,
        UUID squadraId,
        Modulo modulo,
        List<NewTitolareDTO> titolari,
        List<UUID> giocatoriPanchina,
        //tattiche
        MentalitaTattica mentalita,
        List<UUID> rigoristi,
        List<UUID> punizioneRavvicinataCentrale,
        List<UUID> punizioneRavvicinataDestra,
        List<UUID> punizioneRavvicinataSinistra,
        List<UUID> punizioneLontana
) {}

