package pizzamafia.CMbackend.services;

import pizzamafia.CMbackend.entities.Partita;

import java.util.UUID;

public interface SimulazionePartitaService {

    /**
     * Simula una partita a partire dal suo ID.
     * Calcola risultato, marcatori e aggiorna la partita nel database.
     *
     * @param partitaId ID della partita da simulare
     * @return la partita aggiornata con risultato e marcatori
     */
    Partita simula(UUID partitaId);

}
