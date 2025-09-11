package pizzamafia.CMbackend.payloads.partita;

import pizzamafia.CMbackend.entities.EventoPartita;
import pizzamafia.CMbackend.entities.Giocatore;
import pizzamafia.CMbackend.payloads.partita.EventoPartitaRespDTO;

/**
 * EventoPartitaMapper
 * -------------------
 * Converte l'entity EventoPartita nel DTO di risposta.
 */
public final class EventoPartitaMapper {

    private EventoPartitaMapper() {}

    // ==========================================
    // Mapping principale: Entity -> RespDTO
    // ==========================================
    public static EventoPartitaRespDTO toRespDTO(EventoPartita e) {
        Giocatore gp = e.getGiocatorePrincipale();
        Giocatore gs = e.getGiocatoreSecondario();

        return new EventoPartitaRespDTO(
                e.getId(),
                e.getMinuto(),
                e.getSecondo(),
                e.getDurataStimata(),
                e.getTipoEvento(),
                e.getEsito(),
                e.getNote(),
                (e.getPartita() != null ? e.getPartita().getId() : null),
                (gp != null ? gp.getId() : null),
                (gp != null ? gp.getNome() + " " + gp.getCognome() : null),
                (gs != null ? gs.getId() : null),
                (gs != null ? gs.getNome() + " " + gs.getCognome() : null),
                (e.getSquadra() != null ? e.getSquadra().getId() : null)
        );
    }
}
