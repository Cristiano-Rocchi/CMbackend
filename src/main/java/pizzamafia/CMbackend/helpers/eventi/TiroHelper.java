package pizzamafia.CMbackend.helpers.eventi;

import pizzamafia.CMbackend.entities.*;
import pizzamafia.CMbackend.enums.Ruolo;
import pizzamafia.CMbackend.enums.TipoEvento;

import java.util.List;
import java.util.Random;

public class TiroHelper {

    private static final Random random = new Random();

    public static EventoPartita genera(
            int minuto,
            Partita partita,
            Squadra squadraAttaccante,
            Squadra squadraDifendente,
            Giocatore tiratore,
            List<Titolari> titolariDifesa
    ) {
        // =================== 1. Calcola punteggio del tiratore ===================
        StatisticheTecnicheGiocatore st = tiratore.getStatistiche();

        double punteggioTiro = st.getFinalizzazione() * 0.5 +
                st.getIntuito() * 0.2 +
                st.getTiriDaLontano() * 0.15 +
                st.getPosizione() * 0.15 +
                random.nextInt(11); // +0–10

        // =================== 2. Se il tiro è fuori ===================
        if (punteggioTiro < 60) {
            return EventoPartita.builder()
                    .minuto(minuto)
                    .tipoEvento(TipoEvento.TIRO)
                    .giocatorePrincipale(tiratore)
                    .giocatoreSecondario(null)
                    .esito("FUORI")
                    .note("Tiro fuori")
                    .partita(partita)
                    .squadra(squadraAttaccante)
                    .build();
        }

        // =================== 3. Cerca il portiere avversario ===================
        Giocatore portiere = titolariDifesa.stream()
                .map(Titolari::getGiocatore)
                .filter(g -> g.getRuolo() == Ruolo.PORTIERE)
                .findFirst()
                .orElse(titolariDifesa.get(0).getGiocatore()); // fallback

        // =================== 4. Calcola punteggio di parata ===================
        StatisticheTecnicheGiocatore sp = portiere.getStatistiche();

        double parata = sp.getRiflessi() * 0.45 +
                sp.getIntuito() * 0.25 +
                sp.getPosizione() * 0.2 +
                sp.getAgilita() * 0.1 +
                random.nextInt(11); // +0–10

        boolean parato = parata > punteggioTiro;

        // =================== 5. Costruisci evento finale (GOL o PARATA) ===================
        return EventoPartita.builder()
                .minuto(minuto)
                .tipoEvento(parato ? TipoEvento.PARATA : TipoEvento.GOL)
                .giocatorePrincipale(parato ? portiere : tiratore)
                .giocatoreSecondario(parato ? tiratore : null)
                .esito(parato ? "RIUSCITA" : "RETE")
                .note("Esito finale dell'azione")
                .partita(partita)
                .squadra(parato ? squadraDifendente : squadraAttaccante)
                .build();
    }
}
