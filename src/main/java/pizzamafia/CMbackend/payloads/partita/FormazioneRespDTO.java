package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.enums.MentalitaTattica;
import pizzamafia.CMbackend.enums.Modulo;

import java.util.List;
import java.util.UUID;

public record FormazioneRespDTO(
        UUID id,
        UUID partitaId,
        UUID squadraId,
        Modulo modulo,
        MentalitaTattica mentalita,
        List<TitolareRespDTO> titolari,
        List<PanchinaRespDTO> panchina,
        List<UUID> rigoristi,
        List<UUID> punizioneRavvicinataCentrale,
        List<UUID> punizioneRavvicinataDestra,
        List<UUID> punizioneRavvicinataSinistra,
        List<UUID> punizioneLontana
) {}
