package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.Random;

public class PunizioneDirettaHelper {

    private static final Random random = new Random();

    /**
     * Evento "Punizione Diretta" (solo calcolo esito).
     * - Tiratore: calciPiazzati, tiro, concentrazione
     * - Portiere: tuffo, posizione, riflessi
     * - Esito: "GOL" o "PARATA"
     *
     */
    public static EventoPartita genera(
            int minuto,
            int secondo,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            Giocatore tiratore,
            Giocatore portiere
    ) {
        StatisticheTecnicheGiocatore sa = tiratore.getStatistiche();
        StatisticheTecnicheGiocatore sp = portiere.getStatistiche();

        // ===== punteggio tiro (tiratore) =====
        double punteggioTiro =
                safe(sa.getCalciPiazzati()) * 0.45 +
                        safe(sa.getTiro())          * 0.35 +
                        safe(sa.getConcentrazione())* 0.20 +
                        random.nextInt(11); // random

        // ===== punteggio parata (portiere) =====
        double parata =
                safe(sp.getTuffo())    * 0.50 +
                        safe(sp.getPosizione())* 0.30 +
                        safe(sp.getRiflessi()) * 0.20 +
                        random.nextInt(11); // random

        boolean parato = parata > punteggioTiro;

        // ===== evento =====
        return EventoPartita.builder()
                .minuto(minuto)
                .secondo(secondo)
                .durataStimata(3)
                .tipoEvento(TipoEvento.PUNIZIONE)
                .giocatorePrincipale(parato ? portiere : tiratore)
                .giocatoreSecondario(parato ? tiratore : null)
                .esito(parato ? "PARATA" : "GOL")
                .note(parato ? "Punizione diretta parata dal portiere"
                        : "Rete su punizione diretta")
                .partita(partita)
                .squadra(parato ? squadraDifendente : squadraAttaccante)
                .build();
    }

    private static int safe(Integer v) { return v == null ? 0 : v; }
}
